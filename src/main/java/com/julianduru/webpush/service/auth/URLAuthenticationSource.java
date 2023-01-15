package com.julianduru.webpush.service.auth;

import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * created by julian on 14/01/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class URLAuthenticationSource implements UserAuthenticationSource {


    private final NotificationService notificationService;


    @Override
    public Mono<UserIdToken> fetchNotificationToken(WebSocketSession session) {
        try {
            var webSocketQuery = session.getHandshakeInfo().getUri().getRawQuery();
            if (webSocketQuery == null) {
                throw new IllegalStateException("No query parameters found in websocket connection");
            }

            var tokenId = webSocketQuery.split("=")[1];
            if (!StringUtils.hasText(tokenId)) {
                throw new IllegalStateException("Token ID is empty");
            }

            return Mono.justOrEmpty(notificationService.fetchValidatedToken(tokenId));
        }
        catch (Throwable t) {
            log.error("Unable to fetch Notification token", t);
            return Mono.error(t);
        }
    }


    @Override
    public boolean supports(String type) {
        return type.equalsIgnoreCase("url");
    }


}

