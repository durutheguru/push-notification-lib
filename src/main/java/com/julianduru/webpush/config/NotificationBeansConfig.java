package com.julianduru.webpush.config;


import com.julianduru.webpush.config.model.PushServiceConfiguration;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;
import java.security.Security;

/**
 * created by julian
 */
@Configuration
public class NotificationBeansConfig {


    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    @Bean
    public PushService pushService(PushServiceConfiguration serviceConfiguration) throws GeneralSecurityException {
        return new PushService(
            serviceConfiguration.getPublicKey(),
            serviceConfiguration.getPrivateKey()
        );
    }


}

