package com.julianduru.webpush.send.impl;


import com.julianduru.webpush.send.NotificationDispatcher;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.sse.Emitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SSENotificationDispatcher implements NotificationDispatcher {


    private static final String NOTIFICATION_EVENT_TYPE = "notification";


    @Value("${code.config.server-sent-event.retry-timeout-millis}")
    private Long retryInterval;


    private final Emitters sseEmitters;


    @Override
    public List<OperationStatus<String>> sendNotification(PushNotification notification) {
        return sseEmitters.send(
            notification.getUserId(),
            ServerSentEvent.<String>builder()
                .id(notification.getUuid())
                .event(NOTIFICATION_EVENT_TYPE)
                .retry(Duration.ofMillis(retryInterval))
                .data(notification.getMessage())
                .build()
        );
    }


}

