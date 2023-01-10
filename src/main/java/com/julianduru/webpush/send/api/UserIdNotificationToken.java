package com.julianduru.webpush.send.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * created by julian on 01/12/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIdNotificationToken {


    private String userId;


    private String token;


    private LocalDateTime expiresOn;


}
