package com.julianduru.webpush.send.sse;


import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import org.apache.http.HttpResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

/**
 * created by julian
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = {
        SseEmitters.class
    }
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class SseEmittersTest {


    @Autowired
    SseEmitters sseEmitters;



    @Test
    public void testAddingSseEmitter() throws Exception {
        SseEmitter emitter = sseEmitters.add(new SseEmitter());

        Assert.assertNotNull(emitter);

        Map<String, List<SseEmitter>> emitterMap = sseEmitters.getEmitterMap();

        Assert.assertNotNull(emitterMap);
        Assert.assertEquals(1, emitterMap.get(TestConstants.TEST_USER_NAME).size());
    }


    @Test
    public void testSendingNotificationThroughSseEmitter() throws Exception {
        sseEmitters.add(new SseEmitter());

        List<HttpResponse> responseList = sseEmitters.send(new Object());

        HttpResponseListAssert.checkList(responseList);
    }



}
