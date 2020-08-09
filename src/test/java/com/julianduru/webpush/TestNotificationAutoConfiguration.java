package com.julianduru.webpush;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * created by julian
 */
@Configuration
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
        "com.julianduru.webpush.rest"
    }
)
@Import(NotificationAutoConfiguration.class)
public class TestNotificationAutoConfiguration {

}
