package com.julianduru.webpush.send.impl;

import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.UserIdTokenRepository;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * created by julian on 01/12/2022
 */
@Component
public class NotificationTokenRepositoryImpl implements UserIdTokenRepository {


    private final Map<String, UserIdToken> tokenUIDMap = new HashMap<>();


    @Override
    public void saveUserNotificationToken(UserIdToken notificationToken) {
        tokenUIDMap.put(notificationToken.token(), notificationToken);
    }


    @Override
    public Optional<UserIdToken> findByToken(String token) {
        var value = tokenUIDMap.get(token);
        if (value != null) {
            return Optional.of(value);
        }
        else {
            return Optional.empty();
        }
    }


    @Override
    public Collection<UserIdToken> findByUserId(String userId) {
        return null;
    }


}


