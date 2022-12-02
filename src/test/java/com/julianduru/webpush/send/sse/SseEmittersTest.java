package com.julianduru.webpush.send.sse;


import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.send.api.OperationStatus;
import com.julianduru.webpush.send.util.HttpResponseListAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian
 */
@ExtendWith({SpringExtension.class})
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
        int mapCount = sseEmitters.getEmitterMap().size();

        var emitter = sseEmitters.add(TestConstants.TEST_USER_NAME, UUID.randomUUID().toString());

        assertThat(emitter).isNotNull();

        Map<String, UserIDEmittersContainer> emitterMap = sseEmitters.getEmitterMap();

        assertThat(emitterMap).isNotNull();
        assertThat(mapCount + 1).isEqualTo(emitterMap.get(TestConstants.TEST_USER_NAME).allEmitters().size());
    }


    @Test
    public void testSendingNotificationThroughSseEmitter() throws Exception {
        sseEmitters.add(TestConstants.TEST_USER_NAME, UUID.randomUUID().toString());

        List<OperationStatus<String>> responseList = sseEmitters.send(
            Map.of()
        );

        HttpResponseListAssert.checkList(responseList);
    }



}
