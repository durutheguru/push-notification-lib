package com.julianduru.webpush.send.impl;


import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.repository.NotificationSubscriptionRepository;
import com.julianduru.webpush.send.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ejb.AsyncResult;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * created by julian
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebPushNotificationDispatcher implements NotificationDispatcher {


    private final NotificationSubscriptionRepository notificationSubscriptionRepository;

    
    private final PushService pushService;


    @Async
    @Override
    public Future<List<HttpResponse>> sendNotification(Notification notification) {

        List<NotificationSubscription> subscriptions = notificationSubscriptionRepository
            .findByUserId(notification.getUserId());

        List<HttpResponse> responseList = subscriptions.stream().map(
            subscription -> {
                try {
                    nl.martijndwars.webpush.Notification pushNotification = new nl.martijndwars.webpush.Notification(
                        subscription.getEndpoint(),
                        subscription.getPublicKey(),
                        subscription.getAuthToken(),
                        notification.getMessage()
                    );

                    log.debug("Pushing Notification: {}", JSONUtil.asJsonString(pushNotification, ""));

                    HttpResponse response = pushService.send(pushNotification);
                    log.debug("Response Status: {}", response.getStatusLine().getStatusCode());

                    return response;
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    return NotificationDispatcher.defaultFailedNotifResponse(e.getMessage());
                }
            }
        ).filter(
            response -> response != null
        ).collect(Collectors.toList());


        return new AsyncResult<>(responseList);
    }


}
