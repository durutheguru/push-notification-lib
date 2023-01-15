package com.julianduru.webpush;


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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
@EnableWebFluxSecurity
public class TestNotificationAutoConfiguration {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
            .pathMatchers(
                "/ws/**"
            )
            .permitAll()
            .and().cors().and().csrf().disable();

        return http.build();
    }


}
