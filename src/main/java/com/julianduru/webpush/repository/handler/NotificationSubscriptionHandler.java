package com.julianduru.webpush.repository.handler;


import com.julianduru.security.Auth;
import com.julianduru.webpush.entity.NotificationSubscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * created by julian
 */
@Component
@RepositoryEventHandler
public class NotificationSubscriptionHandler {


    @Value("${code.config.notif-subscription.default-expiration-timeout-millis}")
    private long notificationSubscriptionExpiration;



    @HandleBeforeCreate
    public void beforeCreate(NotificationSubscription subscription) {
        subscription.setUserId(Auth.getUserAuthId(true).authUsername);
        if (subscription.getExpirationTime() == null) {
            subscription.setExpirationTime(
                ZonedDateTime.now().plus(notificationSubscriptionExpiration, ChronoUnit.MILLIS)
            );
        }
    }


}
