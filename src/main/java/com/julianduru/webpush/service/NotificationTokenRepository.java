package com.julianduru.webpush.service;

import com.julianduru.webpush.send.api.UserIdNotificationToken;

import java.util.Optional;

/**
 * created by julian on 01/12/2022
 */
public interface NotificationTokenRepository {


    void saveUserSubscriptionToken(UserIdNotificationToken token);


    Optional<UserIdNotificationToken> getUserIdWithToken(String token);


}
