package com.julianduru.webpush.annotation;

import com.julianduru.queueintegrationlib.module.subscribe.annotation.Consumer;
import com.julianduru.webpush.NotificationConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * created by Julian Duru on 04/03/2023
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PushServer {



}
