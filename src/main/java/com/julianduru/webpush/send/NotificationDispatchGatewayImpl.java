package com.julianduru.webpush.send;


import com.julianduru.webpush.send.NotificationDispatchGateway;
import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.sse.SSENotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationDispatchGatewayImpl implements NotificationDispatchGateway {


    private final PushNotificationRepository notificationRepository;


    private final List<NotificationDispatcher> notificationDispatchers;



    public void dispatch(List<PushNotification> notifications) {
        for (var notification : notifications) {
            try {
                var responseList = notificationDispatchers.stream()
                    .map(d -> d.sendNotification(notification))
                    .flatMap(Collection::stream)
                    .toList();

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
