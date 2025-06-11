package com.system.facede.service;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationOptInReportingService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationOptInReportingService.class);

    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationOptInReportingService(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public NotificationOptInReportDTO getNotificationOptInReport() {
        logger.info("Fetching notification opt-in report");
        return preferenceRepository.getOptInCounts();
    }
}