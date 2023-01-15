package com.julianduru.webpush.data;

import com.julianduru.util.test.DataProvider;
import com.julianduru.webpush.send.UserIdToken;
import com.julianduru.webpush.send.UserIdTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * created by julian on 15/01/2023
 */
@Component
@RequiredArgsConstructor
public class UserIdTokenProvider implements DataProvider<UserIdToken> {


    private final UserIdTokenRepository tokenRepository;



    @Override
    public UserIdToken provide() {
        return new UserIdToken(
            faker.code().isbn10(),
            faker.code().isbn10(),
            LocalDateTime.now().plusDays(1)
        );
    }


    public UserIdToken save() {
        var token = provide();
        tokenRepository.saveUserNotificationToken(token);
        return token;
    }


    public UserIdToken save(LocalDateTime expiresOn) {
        var token = new UserIdToken(
            faker.code().isbn10(),
            faker.code().isbn10(),
            expiresOn
        );
        tokenRepository.saveUserNotificationToken(token);
        return token;
    }


}
