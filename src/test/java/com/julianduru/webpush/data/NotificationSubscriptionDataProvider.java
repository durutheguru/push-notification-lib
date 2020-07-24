package com.julianduru.webpush.data;


import com.github.javafaker.Faker;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.rest.NotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * created by julian
 */
@Component
@RequiredArgsConstructor
public class NotificationSubscriptionDataProvider implements DataProvider<NotificationSubscription> {


    private final Faker faker;


    private final NotificationSubscriptionRepository repository;


    @Override
    public NotificationSubscriptionRepository getRepository() {
        return repository;
    }


    @Override
    public NotificationSubscription provide() {
        NotificationSubscription subscription = new NotificationSubscription();

        subscription.setEndpoint(faker.internet().url());
        subscription.setAuthToken("OZSXRj2FTV-owl_nAinwWw");
        subscription.setPublicKey("BHVXmAtpak9Fh1EDob3xRkUpgHXEArXk5OD2dKrJczjKDp5phgcnolZIIU3Odrw68ghglmEqvupWLD6jPAttbRQ");
        subscription.setUserId(faker.name().username());

        return subscription;
    }


    @Override
    public NotificationSubscription provide(NotificationSubscription sample) {
        NotificationSubscription subscription = provide();

        if (sample.getUserId() != null) {
            subscription.setUserId(sample.getUserId());
        }

        return subscription;
    }


}
