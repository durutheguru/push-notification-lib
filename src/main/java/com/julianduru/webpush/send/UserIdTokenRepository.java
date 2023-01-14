package com.julianduru.webpush.send;

import java.util.Collection;
import java.util.Optional;

/**
 * created by julian on 14/01/2023
 */
public interface UserIdTokenRepository {


    Collection<UserIdToken> findByUserId(String userId);


    Optional<UserIdToken> findByToken(String token);


}

