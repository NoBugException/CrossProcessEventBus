package com.nobugexception.hermes.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoHermesEventBus {
    //存储多个被NoHermesSubscribe修饰的方法，一个对象可对应多个方法
    private Map<Object, List<SubscribleMethod>> cacheMap;
    //存储粘贴事件
    private Map<Object, Class<?>> stickyEvents;

    private Handler handler;

    //线程池
    private ExecutorService newCachedThreadPool, newSingleThreadExecutor;

    private NoHermesEventBus(){
        this.cacheMap = new HashMap<>();
        this.stickyEvents = new ConcurrentHashMap<>();
        handler = new Handler(Looper.getMainLooper());
        //并发处理事件
        newCachedThreadPool = Executors.newCachedThreadPool();
        //并行处理事件
        newSingleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    static class NoEventBusHolder{
        public static NoHermesEventBus instance = new NoHermesEventBus();
    }

    /**
     * 获取NoEventBus对象，这个对象必须是单例，因为需要保证cacheMap在内存中唯一
     * @return
     */
    public static NoHermesEventBus getDefault(){
        return NoEventBusHolder.instance;
    }

    /**
     * 注册：其实就是将被NoHermesSubscribe修饰的方法对象存储到cacheMap集合中
     * @param subscriber 注册时绑定的对象
     */
    public void register(Object subscriber) {
        List<SubscribleMethod> subscribleMethods = cacheMap.get(subscriber);
        //如果已经注册，就不需要注册
        if (subscribleMethods == null) {
            subscribleMethods = getSubscribleMethods(subscriber);
            cacheMap.put(subscriber, subscribleMethods);

            //粘贴事件处理
            stickyEvent(subscriber);
        }
    }

    /**
     * 获取当前对象下接收事件的方法信息的集合
     * @param subscriber
     * @return
     */
    private List<SubscribleMethod> getSubscribleMethods(Object subscriber) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> aClass = subscriber.getClass();
        while (aClass != null) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                NoHermesSubscribe annotation = method.getAnnotation(NoHermesSubscribe.class);
                if (annotation == null) {
                    continue;
                }

                //校验方法参数的合法性，被NoHermesSubscribe修饰的方法有且只有一个形式参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("NoEventBus只能有一个形式参数！");
                }

                //获取线程模式
                NoHermesThreadMode NoThreadMode = annotation.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(method, NoThreadMode, parameterTypes[0]);
                list.add(subscribleMethod);
            }
            aClass = aClass.getSuperclass();
        }
        return list;
    }


    /**
     * 粘贴事件处理
     */
    private void stickyEvent(Object subscriber){

        Class<?> aClass = subscriber.getClass();
        while (aClass != null) {
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for (Method method : declaredMethods) {
                NoHermesSubscribe annotation = method.getAnnotation(NoHermesSubscribe.class);
                if (annotation == null) {
                    continue;
                }

                //校验方法参数的合法性，被NoHermesSubscribe修饰的方法有且只有一个形式参数
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new RuntimeException("NoEventBus只能有一个形式参数！");
                }

                Class<?> eventType = parameterTypes[0];

                //校验粘贴性方法开关是否打开
                if(annotation.sticky()){
                    Set<Map.Entry<Object, Class<?>>> entries = stickyEvents.entrySet();
                    for (Map.Entry<Object, Class<?>> entry : entries) {
                        Class<?> candidateEventType = entry.getValue();
                        if (eventType.isAssignableFrom(candidateEventType)) {
                            Object stickyEvent = entry.getKey();
                            if(stickyEvent != null){
                                //发送事件
                                post(stickyEvent);
                            }
                        }
                    }
                }

            }
            aClass = aClass.getSuperclass();
        }
    }

    /**
     * 反注册：其实就是将对应对象的方法集合从集合中删除
     * @param subscriber
     */
    public void unregister(Object subscriber) {
        List<SubscribleMethod> list = cacheMap.get(subscriber);
        //如果获取到
        if (list != null) {
            cacheMap.remove(subscriber);
        }
        //清除粘贴事件
        //stickyEvents.clear();
    }

    /**
     * 发送事件
     * @param obj
     */
    public void post(final Object obj) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            //拿到注册类
            final Object next = iterator.next();
            //获取类中所有添加注解的方法
            List<SubscribleMethod> list = cacheMap.get(next);
            for (final SubscribleMethod subscribleMethod : list) {
                //判断这个方法是否应该接收事件,isAssignableFrom类似于instanceof
                if (subscribleMethod.getEventType().isAssignableFrom(obj.getClass())) {
                    switch (subscribleMethod.getThreadMode()) {
                        case MAIN://接收方法在主线程种情况，事件的处理是阻塞性的，也就是按照顺序梳理
                        case MAIN_ORDERED://接收方法在主线程种情况，事件的处理是非阻塞性的，由于在主线程上，技术上应该无法做到让事件的处理非阻塞，所以就默认认为和MAIN一致
                            //如果接收方法在主线程执行的情况
                            if(Looper.myLooper() == Looper.getMainLooper()){
                                invoke(subscribleMethod, next, obj);
                            } else {
                                //post方法执行在子线程中，接收消息在主线程中
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, next, obj);
                                    }
                                });
                            }
                            break;

                        case ASYNC://接收方法在子线程种情况，适合做耗时操作
                            //post方法执行在主线程中
                            if(Looper.myLooper() == Looper.getMainLooper()){
                                newCachedThreadPool.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, next, obj);
                                    }
                                });
                            } else {
                                //post方法执行在子线程中
                                invoke(subscribleMethod, next, obj);
                            }
                            break;
                        case BACKGROUND://接收方法在子线程种情况，由于消息是按顺序处理的，所以不适合做耗时操作
                            //post方法执行在主线程中
                            if(Looper.myLooper() == Looper.getMainLooper()){
                                newSingleThreadExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, next, obj);
                                    }
                                });
                            } else {
                                //post方法执行在子线程中
                                invoke(subscribleMethod, next, obj);
                            }
                            break;
                        case POSTING:
                            //默认
                            break;
                    }

                }
            }
        }
    }

    /**
     * 发送粘贴性事件
     * @param event
     */
    public void postSticky(Object event) {
        synchronized (stickyEvents) {
            stickyEvents.put(event, event.getClass());
        }

        post(event);
    }

    /**
     * 通过反射，执行对应的方法
     * @param subscribleMethod
     * @param next
     * @param obj
     */
    private void invoke(SubscribleMethod subscribleMethod, Object next, Object obj) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(next, obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
