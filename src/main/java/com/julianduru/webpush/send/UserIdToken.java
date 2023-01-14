package com.julianduru.webpush.send;

import java.time.LocalDateTime;

/**
 * created by julian on 14/01/2023
 */
public record UserIdToken(
    String userId,
    String token,
    LocalDateTime expiresOn
) {

    public boolean isExpired() {
        return expiresOn.isBefore(LocalDateTime.now());
    }

}
