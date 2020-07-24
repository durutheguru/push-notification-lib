package com.julianduru.webpush;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    NotificationAutoConfiguration.class
})
public class ContextLoadingTest {


    @Test
    public void contextLoads() {

    }


}
