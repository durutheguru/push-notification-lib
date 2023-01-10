package com.julianduru.webpush.service;


import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.api.UserIdNotificationToken;
import com.julianduru.webpush.send.sse.Emitters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * created by julian
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    @Value("${code.config.notifications.token.expiry-in-seconds:86400}")
    private Long notificationTokenExpiryIntervalInSeconds;


    @Value("${spring.mvc.async.request-timeout}")
    private Long sseTimeout;


    private final Emitters sseEmitters;


    private final PushNotificationRepository notificationRepository;


    private final NotificationTokenRepository notificationTokenRepository;


    @Override
    public Page<PushNotification> fetchNotifications(String userId, Pageable pageable) {
        return notificationRepository.fetchNotifications(userId, pageable);
    }


    @Override
    public UserIdNotificationToken generateToken(String userId) {
        var token = String.format(
            "%s-%d-%s",
            UUID.randomUUID(),
            System.currentTimeMillis(),
            UUID.randomUUID()
        );

        var uidToken = UserIdNotificationToken.builder()
            .userId(userId)
            .token(token)
            .expiresOn(
                LocalDateTime.now().plusSeconds(
                    notificationTokenExpiryIntervalInSeconds
                )
            )
            .build();

        notificationTokenRepository.saveUserSubscriptionToken(uidToken);
        return uidToken;
    }


    @Override
    public Flux<Object> handleNotificationSubscription(String tokenString) throws IOException {
        var tokenOptional = notificationTokenRepository.getUserIdWithToken(tokenString);
        if (tokenOptional.isEmpty()) {
            throw new SecurityException("Unable to process notification subscription. Invalid token");
        }

        var token = tokenOptional.get();
        if (token.getExpiresOn().isBefore(LocalDateTime.now())) {
            throw new SecurityException("Token has expired");
        }

        return sseEmitters.add(token.getUserId(), token.getToken());
    }


}
