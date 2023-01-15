package com.julianduru.webpush.service;


import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Optional;

/**
 * created by julian
 */
public interface NotificationService {


    Page<PushNotification> fetchNotifications(String userId, Pageable pageable);

    UserIdToken generateToken(String userId);

    Flux<Message<?>> handleSSENotificationSubscription(String token) throws IOException;

    Optional<UserIdToken> fetchValidatedToken(String tokenString);


}
