package com.julianduru.webpush;


import com.julianduru.util.config.CryptoConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
        "com.julianduru.util",
        "com.julianduru.webpush"
    }
)
@EnableAutoConfiguration
@EnableJpaRepositories(
    basePackages = {
        "com.julianduru.webpush.rest"
    }
)
@EnableConfigurationProperties({
    CryptoConfig.class
})
public class NotificationAutoConfiguration {




}
