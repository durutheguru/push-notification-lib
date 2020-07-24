package com.julianduru.webpush.data;


import com.github.javafaker.Faker;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * created by julian
 */
@Component
@RequiredArgsConstructor
public class NotificationDataProvider implements DataProvider<Notification> {


    private final Faker faker;

    private final NotificationRepository repository;


    @Override
    public NotificationRepository getRepository() {
        return repository;
    }


    @Override
    public Notification provide() {
        Notification notification = new Notification();

        notification.setId(faker.random().nextLong());
        notification.setUserId(faker.internet().emailAddress());
        notification.setMessage(faker.address().fullAddress());

        return notification;
    }


    @Override
    public Notification provide(Notification sample) {
        Notification notification = provide();

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

