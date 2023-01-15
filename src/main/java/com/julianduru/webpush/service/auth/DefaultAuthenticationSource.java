package com.julianduru.webpush.service.auth;

import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.UserIdTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

/**
 * created by julian on 14/01/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAuthenticationSource implements UserAuthenticationSource {

    private final UserIdTokenRepository userIdTokenRepository;



    @Override
    public Mono<UserIdToken> fetchNotificationToken(WebSocketSession session) {
        return session.getHandshakeInfo().getPrincipal().flatMap(
            principal -> {
                var optionalUserToken = userIdTokenRepository
                    .findByUserId(principal.getName())
                    .stream().filter(token -> !token.isExpired())
                    .findFirst();

                return optionalUserToken
                    .<Mono<? extends UserIdToken>>map(Mono::just)
                    .orElseGet(Mono::empty);
            }
        );
    }


    @Override
    public boolean supports(String type) {
        return type.equalsIgnoreCase("default");
    }


}

