package com.nobugexception.hermes.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoHermesSubscribe {
    //线程模式
    NoHermesThreadMode threadMode() default NoHermesThreadMode.POSTING;
    //粘贴性事件开关
    boolean sticky() default false;
}
