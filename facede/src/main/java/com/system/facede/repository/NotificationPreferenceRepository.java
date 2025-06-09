package com.system.facede.repository;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.model.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    Optional<NotificationPreference> findByCustomUserId(Long customUserId);
    @Query("SELECT new com.system.facede.dto.NotificationOptInReportDTO(" +
            "SUM(CASE WHEN np.emailEnabled = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN np.smsEnabled = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN np.postalEnabled = true THEN 1 ELSE 0 END)) " +
            "FROM NotificationPreference np")
    NotificationOptInReportDTO getOptInCounts();



    @Modifying
    @Transactional
    @Query("UPDATE NotificationPreference np SET np.emailEnabled = :emailEnabled, np.smsEnabled = :smsEnabled, np.postalEnabled = :postalEnabled WHERE np.customUser.id IN :userIds")
    void updatePreferencesForMultipleUsers(List<Long> userIds, boolean emailEnabled, boolean smsEnabled, boolean postalEnabled);

}
