package com.julianduru.webpush;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * created by julian
 */
@Configuration
@EntityScan(
    basePackages = {
        "com.julianduru.util.jpa",
        "com.julianduru.webpush.entity"
    }
)
@ComponentScan(
    basePackages = {
        "com.julianduru.webpush"
    }
)
@ConditionalOnProperty(
    prefix = "code.auto-configure.webpush",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true
)
public class NotificationAutoConfiguration {




}
