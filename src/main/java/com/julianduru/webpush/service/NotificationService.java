package com.julianduru.webpush.service;


import com.julianduru.webpush.send.api.PushNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * created by julian
 */
public interface NotificationService {


    Page<PushNotification> fetchNotifications(String userId, Pageable pageable);

    String generateToken(String userId);

    Flux<Object> handleNotificationSubscription(String token) throws IOException;


}
