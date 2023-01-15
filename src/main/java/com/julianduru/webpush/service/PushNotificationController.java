package com.julianduru.webpush.service;


import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.api.PushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.security.Principal;
import java.time.Duration;

/**
 * created by julian
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = NotificationConstant.SSE_API_PREFIX + "/notifications")
public class PushNotificationController {


    private final NotificationService notificationService;



    @GetMapping
    public Page<PushNotification> fetchUserNotifications(
        @RequestParam("userId") String userId,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        return notificationService.fetchNotifications(userId, PageRequest.of(page, size));
    }


    @GetMapping("/token")
    public UserIdToken getNotificationSubscriptionToken(@AuthenticationPrincipal Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Unable to determine Authenticated User");
        }

        return notificationService.generateToken(principal.getName());
    }


    @GetMapping(value = "/sse", produces = { MediaType.TEXT_EVENT_STREAM_VALUE })
    public Flux<Object> handleNotificationSubscription(
        ServerHttpResponse response, @RequestParam("token") String token
    ) throws IOException {
        response.getHeaders().add("Cache-Control", "no-store");
        response.getHeaders().add("Connection", "keep-alive");
        var subscription = notificationService.handleSSENotificationSubscription(token);

        return Flux.merge(
            // single instance hot flux
            Flux.interval(Duration.ofSeconds(15)),
            subscription
        )
            .delayElements(Duration.ofMillis(1500))
            .doOnNext(obj -> {
                log.info("Object received: {}", obj);
            });
    }


}


