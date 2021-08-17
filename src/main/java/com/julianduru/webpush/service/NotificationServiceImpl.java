package com.julianduru.webpush.service;


import com.julianduru.util.MapperUtil;
import com.julianduru.util.TimeUtil;
import com.julianduru.util.stream.PullStreamDataRequest;
import com.julianduru.webpush.api.dto.NotificationDTO;
import com.julianduru.webpush.entity.Notification;
import com.julianduru.webpush.rest.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * created by julian
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;


    @Override
    public Page<NotificationDTO> fetchNotifications(String userId, PullStreamDataRequest dataRequest) {
        Page<Notification> notifications;
        if (dataRequest.hasAllTimeStamps()) {
            notifications = notificationRepository.findByUserIdAndTimeAddedBetween(
                userId,
                TimeUtil.zdtFromTimeStamp(dataRequest.getAfterTimeStamp()),
                TimeUtil.zdtFromTimeStamp(dataRequest.getBeforeTimeStamp()),
                dataRequest.getPageable()
            );
        }

        else if (dataRequest.hasAfterTimeStamp()) {
            notifications = notificationRepository.findByUserIdAndTimeAddedAfter(
                userId,
                TimeUtil.zdtFromTimeStamp(dataRequest.getAfterTimeStamp()),
                dataRequest.getPageable()
            );
        }

        else if (dataRequest.hasBeforeTimeStamp()) {
            notifications = notificationRepository.findByUserIdAndTimeAddedBefore(
                userId,
                TimeUtil.zdtFromTimeStamp(dataRequest.getBeforeTimeStamp()),
                dataRequest.getPageable()
            );
        }

        else {
            notifications = notificationRepository.findByUserId(
                userId, dataRequest.getPageable()
            );
        }

        return MapperUtil.map(notifications, NotificationDTO.class);
    }


}
