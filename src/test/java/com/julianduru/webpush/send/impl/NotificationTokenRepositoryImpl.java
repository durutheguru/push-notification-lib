package com.julianduru.webpush.send.impl;

import com.julianduru.webpush.send.api.UserIdNotificationToken;
import com.julianduru.webpush.service.NotificationTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * created by julian on 01/12/2022
 */
@Component
public class NotificationTokenRepositoryImpl implements NotificationTokenRepository {

    private final Map<String, UserIdNotificationToken> tokenUIDMap = new HashMap<>();


    @Override
    public void saveUserSubscriptionToken(UserIdNotificationToken notificationToken) {
        tokenUIDMap.put(notificationToken.getToken(), notificationToken);
    }


    @Override
    public Optional<UserIdNotificationToken> getUserIdWithToken(String token) {
        var value = tokenUIDMap.get(token);
        if (value != null) {
            return Optional.of(value);
        }
        else {
            return Optional.empty();
        }
    }


}

