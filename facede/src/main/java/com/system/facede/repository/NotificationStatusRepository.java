package com.system.facede.repository;

import com.system.facede.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {
    List<NotificationStatus> findByCustomerUserId(Long customerUserId);
}
