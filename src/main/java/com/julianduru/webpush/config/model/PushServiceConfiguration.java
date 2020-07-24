package com.julianduru.webpush.config.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * created by julian
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "code.config.web-push-service")
public class PushServiceConfiguration {


    @NotEmpty(message = "Web Push Private Key must be supplied")
    private String privateKey;


    @NotEmpty(message = "Web Push Public Key must be supplied")
    private String publicKey;


}
