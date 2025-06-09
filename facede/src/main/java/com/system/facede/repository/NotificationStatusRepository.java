package com.system.facede.repository;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    List<NotificationStatus> findByCustomUserId(Long customUserId);

    // Custom query to aggregate notification status counts
    @Query("SELECT new com.system.facede.dto.NotificationStatusReportDTO(" +
            "SUM(CASE WHEN ns.status = 'DELIVERED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ns.status = 'FAILED' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN ns.status = 'PENDING' THEN 1 ELSE 0 END)) " +
            "FROM NotificationStatus ns")
    NotificationStatusReportDTO getNotificationStatusCounts();
}
