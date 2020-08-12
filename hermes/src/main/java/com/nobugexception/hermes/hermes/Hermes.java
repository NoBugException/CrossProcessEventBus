package com.nobugexception.hermes.hermes;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.google.gson.Gson;
import com.nobugexception.hermes.EventMessage;
import com.nobugexception.hermes.IHermesService;
import com.nobugexception.hermes.Request;
import com.nobugexception.hermes.Responce;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨进程通信的核心类（在客户端进程和服务器进程各有一个对象）
 */
public class Hermes {

    private Context mContext;

    // 客户端是否连接服务器
    private boolean isConnect = false;

    // 把从服务器拿到的代理对象保存到数组中
    private final ConcurrentHashMap<Class<? extends HermesService>, IHermesService> mHermesService = new ConcurrentHashMap<>();

    private Hermes(){}

    // 静态内部类单例
    public static Hermes getDefault(){
        return NoHermesHolder.instance;
    }

    /**
     *  初始化
     * @param mContext
     */
    public void init(Context mContext) {
        this.mContext = mContext;
    }

    static class NoHermesHolder{
        public static Hermes instance = new Hermes();
    }

    /**
     * 自定义ServiceConnection
     */
    private class HermesServiceConnection implements ServiceConnection {

        private Class<? extends HermesService> mServiceClass;

        public HermesServiceConnection(Class<? extends HermesService> serviceClass){
            this.mServiceClass = serviceClass;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnect = true;
            IHermesService iEventMessage = IHermesService.Stub.asInterface(service);
            if(iEventMessage != null){
                mHermesService.put(mServiceClass, iEventMessage);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
            mHermesService.remove(mServiceClass);
        }
    }

    /**
     * 客户端进程，连接服务器进程
     * 可能会连接多个服务，所以需要传递服务类字节码，以区分连接不同的服务
     * 连接成功会保存代理对象，连接失败会移除代理对象
     */
    private void connect(Class<? extends HermesService> serviceClass) {
        Intent intent = new Intent(mContext, serviceClass);
        mContext.bindService(intent, new HermesServiceConnection(serviceClass), Context.BIND_AUTO_CREATE);
    }

    /**
     * 如果客户端进程没有和服务端进程连接，则返回“服务未连接”
     * @param serviceClass
     * @return
     */
    private Responce responceIfNotConnect(Class<? extends HermesService> serviceClass){
        // 如果缓存中没有代理对象，那么重连
        connect(serviceClass);
        Responce responce = new Responce();
        responce.setResultCode(ResultCode.ERROR_CODE_1);
        responce.setData("");
        responce.setErrorMsg(ResultCode.ERROR_MSG_1);
        return responce;
    }

    /**
     * 如果服务器进程没有任何返回，则返回“服务未响应”
     * @param serviceClass
     * @return
     */
    private Responce nullResponce(Class<? extends HermesService> serviceClass){
        // 如果缓存中没有代理对象，那么重连
        connect(serviceClass);
        Responce responce = new Responce();
        responce.setResultCode(ResultCode.ERROR_CODE_2);
        responce.setData("");
        responce.setErrorMsg(ResultCode.ERROR_MSG_2);
        return responce;
    }

    /**
     * 客户端进程请求服务器
     * @param request
     */
    public Responce send(Request request) {
        Class clazz = null;
        try {
            clazz = Class.forName(request.getServiceFullClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isConnect){
            IHermesService iHermesService = mHermesService.get(clazz);
            if (iHermesService == null){
                return nullResponce(clazz);
            } else {
                Responce responce = null;
                try {
                    responce = iHermesService.send(request);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                return responce;
            }
        } else {
            return responceIfNotConnect(clazz);
        }
    }

    /**
     * 发送消息
     * @param hermesMessage
     */
    public void post(Object hermesMessage) {
        if(hermesMessage == null){
            return;
        }
        if (isConnect){
            IHermesService iHermesService = mHermesService.get(HermesService.class);
            if (iHermesService == null){
                return;
            } else {
                EventMessage eventMessage = new EventMessage();
                eventMessage.setClassFullName(hermesMessage.getClass().getName());
                eventMessage.setData(new Gson().toJson(hermesMessage));
                eventMessage.setSticky(false);
                try {
                    iHermesService.post(eventMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else {
            connect(HermesService.class);
        }
    }

    /**
     * 发送粘性消息
     * @param hermesMessage
     */
    public void postSticky(Object hermesMessage){
        if(hermesMessage == null){
            return;
        }
        if (isConnect){
            IHermesService iHermesService = mHermesService.get(HermesService.class);
            if (iHermesService == null){
                return;
            } else {
                EventMessage eventMessage = new EventMessage();
                eventMessage.setClassFullName(hermesMessage.getClass().getName());
                eventMessage.setData(new Gson().toJson(hermesMessage));
                eventMessage.setSticky(true);
                try {
                    iHermesService.post(eventMessage);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else {
            connect(HermesService.class);
        }
    }

    /**
     * 注册
     * @param clazz
     */
    public void register(Class clazz){

    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
