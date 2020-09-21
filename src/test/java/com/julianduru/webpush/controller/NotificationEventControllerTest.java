package com.julianduru.webpush.controller;


import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.event.NotificationEventTestHelper;
import com.julianduru.webpush.rest.BaseRestIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * created by julian
 */
@SpringBootTest(
    classes = {
        NotificationAutoConfiguration.class
    },
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class NotificationEventControllerTest extends BaseRestIntegrationTest {


    @LocalServerPort
    private int serverPort;


    @Autowired
    private NotificationEventTestHelper notificationEventTestHelper;



    @Test
    public void testRegisteringForWebSocketSSEs() throws Exception {
        try {
            String expectedContent = "data:Established Connection...\n\n";

            MvcResult mvcResult = mockMvc.perform(
                get(NotificationConstant.SSE_API_PREFIX + "/notification?testing=true").contentType(MediaType.ALL)
            ).andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(request().asyncStarted())
                .andExpect(header().string("Content-Type", "text/event-stream;charset=UTF-8"))
                .andExpect(content().string(expectedContent))
                .andReturn();

            mvcResult.getRequest().getAsyncContext().setTimeout(1000L);
            mvcResult.getAsyncResult();
        }
        catch (IllegalStateException e) {
            assertThat(e.getMessage()).startsWith("Async result for handler");
        }


//            .andExpect(request().asyncResult(nullValue()))

//        mockMvc.perform(get(path).contentType(MediaType.ALL))
//            .andExpect(status().isOk())
//            .andExpect(request().asyncStarted())
//            .andExpect(request().asyncResult(nullValue()))
//            .andExpect(header().string("Content-Type", "text/event-stream"))
//            .andExpect(content().string(expectedContent))

//        SseTestClient sseClient = new SseTestClient("http://localhost:" + serverPort + NotificationConstant.SSE_API_PREFIX + "/notification").connect();

//        notificationEventTestHelper.testPublishedEventToUsername(TestConstants.TEST_USER_NAME);

//        await().atMost(Duration.ofSeconds(2)).until(sseClient::hasNewEvent);
    }



}
