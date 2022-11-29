package com.julianduru.webpush.rest;


import com.julianduru.security.Auth;
import com.julianduru.security.config.CORSWebSecurityConfig;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.config.DataRestConfig;
import com.julianduru.util.test.JpaDataProvider;
import com.julianduru.webpush.NotificationAutoConfiguration;
import com.julianduru.webpush.TestConstants;
import com.julianduru.webpush.data.NotificationSubscriptionDataProvider;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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
        DataRestConfig.class,
        CORSWebSecurityConfig.class,
        NotificationSubscriptionDataProvider.class,
        NotificationAutoConfiguration.class
    }
)
@WithMockUser(username = TestConstants.TEST_USER_NAME)
public class NotificationSubscriptionResourceTest extends BaseRestIntegrationTest {


    @Autowired
    JpaDataProvider<NotificationSubscription> dataProvider;


    @Autowired
    NotificationSubscriptionRepository subscriptionRepository;


    @Test
    public void testAddingNewNotificationSubscription() throws Exception {
        NotificationSubscription sample = new NotificationSubscription();
        sample.setUserId(Auth.getUserAuthId(true).authUsername);

        NotificationSubscription subscription = dataProvider.provide(sample);
        subscription.setUserId(null);

        mockMvc.perform(
            post(DATA_REST_BASE_PATH + NotificationSubscriptionRepository.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.asJsonString(subscription))
        ).andDo(print())
            .andExpect(status().is2xxSuccessful());

        Optional<NotificationSubscription> persistedSubscription = subscriptionRepository
            .findByEndpoint(subscription.getEndpoint());

        subscription.setUserId(sample.getUserId());

        assertThat(persistedSubscription.isPresent()).isTrue();
        assertThat(persistedSubscription.get())
            .isEqualToComparingOnlyGivenFields(subscription, "endpoint", "publicKey", "authToken", "userId");
    }


    @Test
    @Disabled
    public void testAddingExistingNotificationSubscriptionEndpoint() throws Exception {
        NotificationSubscription sample = new NotificationSubscription();
        sample.setEndpoint("http://sample.com");

        dataProvider.save(sample);


        NotificationSubscription subscription = dataProvider.provide(sample);

        mockMvc.perform(
            post(DATA_REST_BASE_PATH + NotificationSubscriptionRepository.PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONUtil.asJsonString(subscription))
        ).andDo(print())
            .andExpect(status().is4xxClientError());
    }


}
