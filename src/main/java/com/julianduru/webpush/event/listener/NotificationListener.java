package com.julianduru.webpush.event.listener;


import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.event.NotificationDispatchGateway;
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


    private final NotificationDispatchGateway notificationDispatchGateway;


    @TransactionalEventListener(fallbackExecution = true)
    public void handleNotificationEvent(NotificationEvent event) {
        List<Notification> notifications = notificationRepository.saveAll(Notification.listFrom(event));
        notificationDispatchGateway.dispatch(notifications);
    }


}
