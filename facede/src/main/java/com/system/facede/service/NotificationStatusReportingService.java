package com.system.facede.service;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.repository.NotificationStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationStatusReportingService {

    private final NotificationStatusRepository notificationStatusRepository;

    public NotificationStatusReportingService(NotificationStatusRepository notificationStatusRepository) {
        this.notificationStatusRepository = notificationStatusRepository;
    }

    public NotificationStatusReportDTO getNotificationStatusReport() {
        return notificationStatusRepository.getNotificationStatusCounts();
    }
}
