package com.julianduru.webpush.send.impl;


import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.exception.ServerSentEventException;
import com.julianduru.webpush.send.NotificationDispatcher;
import com.julianduru.webpush.send.sse.Emitters;
import com.julianduru.webpush.send.sse.SseEmitters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ejb.AsyncResult;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Future;

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


    @Async
    @Override
    public Future<List<HttpResponse>> sendNotification(Notification notification) {
        List<HttpResponse> responseList = sseEmitters.send(
            notification.getUserId(),

            ServerSentEvent.<Notification>builder()
                .id(notification.getId().toString())
                .event(notification.getMessage())
                .retry(Duration.ofMillis(retryInterval))
                .data(notification).build()
        );

        return new AsyncResult<>(responseList);
    }


}
