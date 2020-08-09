package com.julianduru.webpush.rest;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * created by julian
 */
@AutoConfigureWebMvc
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(
    properties = {
        "code.config.enable.cors-config=true"
    }
)
public abstract class BaseRestIntegrationTest {


    @Value("${spring.data.rest.basePath}")
    protected String DATA_REST_BASE_PATH;


    @Autowired
    protected MockMvc mockMvc;



}
