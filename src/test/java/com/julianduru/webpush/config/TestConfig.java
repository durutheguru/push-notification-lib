package com.julianduru.webpush.config;


import com.github.javafaker.Faker;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * created by julian
 */
@TestConfiguration
public class TestConfig {


    @Bean
    public Faker faker() {
        return new Faker();
    }


}
