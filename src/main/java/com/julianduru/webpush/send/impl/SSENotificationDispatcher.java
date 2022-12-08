package com.julianduru.webpush.send.impl;


import com.julianduru.webpush.send.NotificationDispatcher;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.sse.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SSENotificationDispatcher implements NotificationDispatcher {


    @Value("${code.config.server-sent-event.retry-timeout-millis}")
    private Long retryInterval;


    private final Emitters sseEmitters;


    @Override
    public List<OperationStatus<String>> sendNotification(PushNotification notification) {
        return sseEmitters.send(
            notification.getUserId(),
            Map.of(
                "uuid", notification.getUuid(),
                "event", notification.getType(),
                "data", notification.getMessage()
            )
        );
    }


}

