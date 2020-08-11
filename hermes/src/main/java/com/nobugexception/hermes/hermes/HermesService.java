package com.nobugexception.hermes.hermes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;

import com.nobugexception.hermes.EventMessage;
import com.nobugexception.hermes.IHermesService;
import com.nobugexception.hermes.Request;
import com.nobugexception.hermes.Responce;
import com.nobugexception.hermes.eventbus.NoHermesEventBus;

import java.lang.reflect.Proxy;

public abstract class HermesService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new HermesBinder();
    }

    class HermesBinder extends IHermesService.Stub{

        @Override
        public Responce send(Request request) throws RemoteException { // 客户端进程将数据发送给服务器进程
            if(request == null) {
                Responce responce = new Responce();
                responce.setData("");
                responce.setErrorMsg(ResultCode.ERROR_MSG_3);
                responce.setResultCode(ResultCode.ERROR_CODE_3);
                return responce;
            } else {
                return getResponce(request);
            }
        }

        @Override
        public void post(EventMessage event) throws RemoteException {

            NoHermesEventBus.getDefault().post(event);
        }
    }

    protected abstract Responce getResponce(Request request);

}
