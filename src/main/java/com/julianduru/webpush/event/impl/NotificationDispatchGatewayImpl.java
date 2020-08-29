package com.julianduru.webpush.event.impl;


import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.event.NotificationDispatchGateway;
import com.julianduru.webpush.rest.NotificationRepository;
import com.julianduru.webpush.send.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDispatchGatewayImpl implements NotificationDispatchGateway {


    private final NotificationRepository notificationRepository;


    private final List<NotificationDispatcher> notificationDispatchers;


    @Async
    public void dispatch(List<Notification> notifications) {
        sendNotificationsToDispatchers(notifications);
    }


    private void sendNotificationsToDispatchers(List<Notification> notifications) {
        for (Notification notification : notifications) {
            try {
                var responseList = sendNotificationToDispatchers(notification);

                long successfulCount = responseList.stream()
                    .filter(r -> (r.getStatusLine().getStatusCode() + "").startsWith("2"))
                    .count();

                notificationRepository.save(notification.received(successfulCount > 0));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


    private List<HttpResponse> sendNotificationToDispatchers(Notification notification) {
        List<HttpResponse> responseList = new ArrayList<>();

        Future<List<HttpResponse>> responseListFuture;
        List<Future<List<HttpResponse>>> responseListFutures = new ArrayList<>();
        for (NotificationDispatcher dispatcher : notificationDispatchers) {
            responseListFuture = dispatcher.sendNotification(notification);
            if (responseListFuture != null) {
                responseListFutures.add(responseListFuture);
            }
        }

        responseListFutures.forEach(future -> {
            try {
                responseList.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        });

        return responseList;
    }


}
