package com.system.facede.service;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.repository.NotificationPreferenceRepository;
import org.springframework.stereotype.Service;


@Service
public class NotificationOptInReportingService {

    private final NotificationPreferenceRepository preferenceRepository;

    public NotificationOptInReportingService(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public NotificationOptInReportDTO getNotificationOptInReport() {
        return preferenceRepository.getOptInCounts();
    }
}
