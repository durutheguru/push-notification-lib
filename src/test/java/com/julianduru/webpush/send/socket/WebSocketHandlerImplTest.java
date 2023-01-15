package com.julianduru.webpush.send.socket;

import com.github.javafaker.Faker;
import com.julianduru.webpush.TestNotificationAutoConfiguration;
import com.julianduru.webpush.data.UserIdTokenProvider;
import com.julianduru.webpush.send.UserIdTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * created by julian on 15/01/2023
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestNotificationAutoConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class WebSocketHandlerImplTest {


    private Faker faker = new Faker();


    @Autowired
    private UserIdTokenRepository userIdTokenRepository;


    @Autowired
    private UserIdTokenProvider userIdTokenProvider;


    private WebSocketClient webSocketClient = new ReactorNettyWebSocketClient();



    @Test
    public void handlingSocketConnectionRequest() throws Exception {
        var token = userIdTokenProvider.save();
        var counter = new AtomicLong();
        final var incomingMessageList = new ArrayList<>();

        webSocketClient
            .execute(
                URI.create(String.format("ws://localhost:11111/ws/connect?token=%s", token.token())),
                (session) -> {
                    var out = Mono.just(
                        session.textMessage("test message from client")
                    );

                    var in = session.receive()
                        .map(WebSocketMessage::getPayloadAsText);

                    return session
                        .send(out)
                        .thenMany(in)
                        .doOnNext(str -> {
                            counter.incrementAndGet();
                            incomingMessageList.add(str);
                        })
                        .then();
                }
            )
            .subscribe();

        await().atMost(Duration.ofSeconds(1)).until(() -> counter.get() > 0);

        assertThat(counter.get()).isGreaterThan(1);
        assertThat(incomingMessageList)
            .containsExactlyInAnyOrder(
                "Connection Established..",
                "Waiting for Events.."
            );
    }


    @Test
    public void handlingSocketConnectionRequestForExpiredToken() throws Exception {
        var token = userIdTokenProvider.save(LocalDateTime.now().minusDays(2));
        var counter = new AtomicLong();
        final var incomingMessageList = new ArrayList<>();

        webSocketClient
            .execute(
                URI.create(String.format("ws://localhost:11111/ws/connect?token=%s", token.token())),
                (session) -> {
                    var out = Mono.just(
                        session.textMessage("test message from client")
                    );

                    var in = session.receive()
                        .map(WebSocketMessage::getPayloadAsText);

                    return session
                        .send(out)
                        .thenMany(in)
                        .doOnNext(str -> {
                            counter.incrementAndGet();
                            incomingMessageList.add(str);
                        })
                        .then();
                }
            )
            .subscribe();

//        await().atMost(Duration.ofSeconds(1));
        Thread.sleep(1000L);

        assertThat(counter.get()).isEqualTo(0);
    }


}
