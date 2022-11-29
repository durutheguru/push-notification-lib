package com.julianduru.webpush.send.impl;


import com.julianduru.webpush.send.NotificationDispatchGateway;
import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDispatchGatewayImpl implements NotificationDispatchGateway {


    private final PushNotificationRepository notificationRepository;


    private final SSENotificationDispatcher notificationDispatcher;



    public void dispatch(List<PushNotification> notifications) {
        sendNotificationsToDispatcher(notifications);
    }


    private void sendNotificationsToDispatcher(List<PushNotification> notifications) {
        for (var notification : notifications) {
            try {
                var responseList = notificationDispatcher.sendNotification(notification);

                var successfulCount = responseList.stream()
                    .filter(r -> r.is(OperationStatus.Value.SUCCESS))
                    .count();

                log.info("Notification to user {}. Received by: {}", notification.getUserId(), successfulCount);

                notificationRepository.save(notification);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }


}
