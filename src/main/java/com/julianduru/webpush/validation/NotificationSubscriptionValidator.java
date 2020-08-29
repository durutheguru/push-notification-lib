package com.julianduru.webpush.validation;


import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.julianduru.util.validation.CustomRestValidator;
import com.julianduru.webpush.entity.NotificationSubscription;
import com.julianduru.webpush.rest.NotificationSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Set;

/**
 * created by julian
 */
@Component
@RequiredArgsConstructor
public class NotificationSubscriptionValidator implements CustomRestValidator {


    private final NotificationSubscriptionRepository subscriptionRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return NotificationSubscription.class.isAssignableFrom(clazz);
    }


    @Override
    public void validate(Object target, Errors errors) {
        var subscription = (NotificationSubscription) target;
        
        if (existingSubscriptionEndpoint(subscription)) {
            errors.reject("existing", String.format("Endpoint '%s' already registered", subscription));
        }
    }


    private boolean existingSubscriptionEndpoint(NotificationSubscription subscription) {
        return Strings.isNullOrEmpty(subscription.getEndpoint()) &&
            subscriptionRepository.existsByEndpoint(subscription.getEndpoint());
    }


    @Override
    public Set<String> getEvents() {
        return Sets.newHashSet("beforeCreate");
    }


}
