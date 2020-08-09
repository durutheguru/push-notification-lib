package com.julianduru.webpush;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
    TestNotificationAutoConfiguration.class
})
public abstract class BaseServiceIntegrationTest {

}
