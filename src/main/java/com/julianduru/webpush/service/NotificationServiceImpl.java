package com.julianduru.webpush.service;


import com.julianduru.webpush.send.PushNotificationRepository;
import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.UserIdTokenRepository;
import com.julianduru.webpush.send.api.Message;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.sse.Emitters;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
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


    private final UserIdTokenRepository userIdTokenRepository;


    @Override
    public Page<PushNotification> fetchNotifications(String userId, Pageable pageable) {
        return notificationRepository.fetchNotifications(userId, pageable);
    }


    @Override
    public UserIdToken generateToken(String userId) {
        var token = String.format(
            "%s-%d-%s",
            UUID.randomUUID(),
            System.currentTimeMillis(),
            UUID.randomUUID()
        );

        var uidToken = new UserIdToken(
            userId, token,
            LocalDateTime.now().plusSeconds(
                notificationTokenExpiryIntervalInSeconds
            )
        );

        userIdTokenRepository.saveUserNotificationToken(uidToken);
        return uidToken;
    }


    @Override
    public Flux<Message<?>> handleSSENotificationSubscription(String tokenString) throws IOException {
        var token = fetchValidatedToken(tokenString).get();
        return sseEmitters.add(token.userId(), token.token());
    }


    public Optional<UserIdToken> fetchValidatedToken(String tokenString) {
        var tokenOptional = userIdTokenRepository.findByToken(tokenString);
        if (tokenOptional.isEmpty()) {
            throw new SecurityException("Unable to process notification subscription. Invalid token");
        }

        var token = tokenOptional.get();
        if (token.expiresOn().isBefore(LocalDateTime.now())) {
            throw new SecurityException("Token has expired. " + token.token());
        }

        return tokenOptional;
    }


}


