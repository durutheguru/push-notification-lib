package com.julianduru.webpush.api;


import com.julianduru.webpush.exception.NotificationSerializationException;

/**
 * created by julian
 */
public interface NotificationPayloadSerializer {


    String serialize(NotificationPayload<?> payload) throws NotificationSerializationException;


}
