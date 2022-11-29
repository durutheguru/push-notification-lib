package com.julianduru.webpush.data;


import com.github.javafaker.Faker;
import com.julianduru.util.test.JpaDataProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * created by julian
 */
@Component
@RequiredArgsConstructor
public class NotificationDataProvider implements JpaDataProvider<Notification> {


    private final Faker faker;

    private final NotificationRepository repository;


    @Override
    public NotificationRepository getRepository() {
        return repository;
    }


    @Override
    public Notification provide() {
        var notification = new Notification();

        notification.setId(faker.random().nextLong());
        notification.setUserId(faker.internet().emailAddress());
        notification.setMessage(faker.address().fullAddress());

        return notification;
    }


    @Override
    public Notification provide(Notification sample) {
        var notification = provide();

        if (sample.getId() != null) {
            notification.setId(sample.getId());
        }

        if (sample.getUserId() != null) {
            notification.setUserId(sample.getUserId());
        }

        if (sample.getMessage() != null) {
            notification.setMessage(sample.getMessage());
        }

        return notification;
    }


}

