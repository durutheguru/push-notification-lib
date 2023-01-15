package com.julianduru.webpush.service.auth;

import com.julianduru.webpush.send.UserIdToken;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * created by julian on 14/01/2023
 */
public interface UserAuthenticationSource {

    Mono<UserIdToken> fetchNotificationToken(WebSocketSession session);

    boolean supports(String type);

}

