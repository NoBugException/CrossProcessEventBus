package com.nobugexception.hermes.eventbus;

import java.lang.reflect.Method;

/**
 * 记录方法的信息
 */
public class SubscribleMethod {
    //注册方法
    private Method method;
    //线程模式
    private NoHermesThreadMode threadMode;
    //参数类型
    private Class<?> eventType;

    public SubscribleMethod(Method method, NoHermesThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public NoHermesThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(NoHermesThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}
