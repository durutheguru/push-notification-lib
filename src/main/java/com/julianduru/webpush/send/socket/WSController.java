package com.julianduru.webpush.send.socket;

import com.github.javafaker.Faker;
import com.julianduru.webpush.send.api.PushNotification;
import com.julianduru.webpush.send.api.UserIdNotificationToken;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * created by julian on 10/01/2023
 */
//@Controller
public class WSController {


//    @MessageMapping("/fetch")
//    @SendTo("/topic/notifications")
    public PushNotification fetch(UserIdNotificationToken message) {
        var faker = new Faker();

        return PushNotification.builder()
            .type("NOTIF")
            .message(faker.code().isbn10())
            .build();
    }


}


