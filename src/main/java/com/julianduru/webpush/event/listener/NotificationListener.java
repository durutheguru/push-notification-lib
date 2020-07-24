package com.julianduru.webpush.event.listener;


import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.event.NotificationEvent;
import com.julianduru.webpush.rest.NotificationRepository;
import com.julianduru.webpush.send.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

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
public class NotificationListener {


    private final NotificationRepository notificationRepository;


    private final List<NotificationDispatcher> notificationDispatchers;


    @TransactionalEventListener(fallbackExecution = true)
    public void handleNotificationEvent(NotificationEvent event) {
        try {
            Notification notification = notificationRepository.save(Notification.from(event));

            List<HttpResponse> responseList = sendNotificationToDispatchers(notification);

            long successfulCount = responseList.stream()
                .filter(r -> (r.getStatusLine().getStatusCode() + "").startsWith("2"))
                .count();

            notificationRepository.save(notification.received(successfulCount > 0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

        responseListFutures.stream().forEach(future -> {
            try {
                responseList.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error(e.getMessage(), e);
            }
        });

        return responseList;
    }


}
