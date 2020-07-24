package com.julianduru.webpush.rest;


import com.julianduru.security.Auth;
import com.julianduru.security.config.CORSWebSecurityConfig;
import com.julianduru.util.JSONUtil;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.data.DataProvider;
import com.julianduru.webpush.data.NotificationSubscriptionDataProvider;
import com.julianduru.webpush.entity.NotificationSubscription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * created by julian
 */
@SpringBootTest(
    classes = {
        CORSWebSecurityConfig.class,
        NotificationSubscriptionDataProvider.class,
        NotificationAutoConfiguration.class
    }
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class NotificationSubscriptionResourceTest extends BaseRestIntegrationTest {


    @Autowired
    DataProvider<NotificationSubscription> dataProvider;


    @Autowired
    NotificationSubscriptionRepository subscriptionRepository;


    @Test
    public void testAddingNewNotificationSubscription() throws Exception {
        NotificationSubscription sample = new NotificationSubscription();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        NotificationSubscription subscription = dataProvider.provide(sample);

        mockMvc.perform(
            post(API_BASE_PATH + NotificationSubscriptionRepository.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.asJsonString(subscription))
        ).andDo(print())
            .andExpect(status().is2xxSuccessful());

        Optional<NotificationSubscription> persistedSubscription = subscriptionRepository
            .findByEndpoint(subscription.getEndpoint());

        assertThat(persistedSubscription.isPresent()).isTrue();
        assertThat(persistedSubscription.get())
            .isEqualToComparingOnlyGivenFields(subscription, "endpoint", "publicKey", "authToken", "userId");
    }


}
