package com.julianduru.webpush;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by julian
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {
    NotificationAutoConfiguration.class
})
public class ContextLoadingTest {


    @Test
    public void contextLoads() {

    }


}
