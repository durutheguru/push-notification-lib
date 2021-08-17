package com.julianduru.webpush.controller;


import com.julianduru.util.stream.PullStreamData;
import com.julianduru.util.stream.PullStreamDataRequest;
import com.julianduru.webpush.NotificationConstant;
import com.julianduru.webpush.api.dto.NotificationDTO;
import com.julianduru.webpush.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * created by julian
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(NotificationController.PATH)
public class NotificationController {


    public static final String PATH = NotificationConstant.API_PREFIX + "/user_notifications";


    private final NotificationService notificationService;



    @GetMapping
    public PullStreamData<NotificationDTO> fetchUserNotifications(
        @RequestParam("userId") String userId,
        @RequestParam(required = false, defaultValue = "-1") Long before,
        @RequestParam(required = false, defaultValue = "-1") Long after,
        @RequestParam(required = false, defaultValue = "0") Integer page,
        @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        return new PullStreamData<>(
            notificationService.fetchNotifications(
                userId,
                PullStreamDataRequest.builder()
                    .afterTimeStamp(after)
                    .beforeTimeStamp(before)
                    .pageable(PageRequest.of(page, size))
                    .build()
            )
        );
    }


}


