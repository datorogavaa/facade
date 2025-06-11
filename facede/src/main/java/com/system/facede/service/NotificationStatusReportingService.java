package com.system.facede.service;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.repository.NotificationStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationStatusReportingService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStatusReportingService.class);

    private final NotificationStatusRepository notificationStatusRepository;

    public NotificationStatusReportingService(NotificationStatusRepository notificationStatusRepository) {
        this.notificationStatusRepository = notificationStatusRepository;
    }

    public NotificationStatusReportDTO getNotificationStatusReport() {
        logger.info("Fetching notification status report");
        return notificationStatusRepository.getNotificationStatusCounts();
    }
}