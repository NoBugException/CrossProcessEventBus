package com.nobugexception.hermes.hermes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import android.text.TextUtils;
import android.util.Log;

public class HermesInvocationHander implements InvocationHandler {

    private Object object;

    public HermesInvocationHander(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("yunchong", "11111111111111111111111111");
        return method.invoke(object, args);
    }
}
