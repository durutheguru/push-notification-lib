package com.julianduru.webpush.api.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.api.NotificationPayload;
import com.julianduru.webpush.api.NotificationPayloadSerializer;
import com.julianduru.webpush.exception.NotificationSerializationException;
import org.springframework.stereotype.Component;

/**
 * created by julian
 */
@Component
public class NotificationPayloadSerializerImpl implements NotificationPayloadSerializer {


    @Override
    public String serialize(NotificationPayload<?> payload) {
        try {
            return JSONUtil.asJsonString(payload);
        }
        catch (JsonProcessingException e) {
            throw new NotificationSerializationException("Unable to serialize Notification payload", e);
        }
    }


}
