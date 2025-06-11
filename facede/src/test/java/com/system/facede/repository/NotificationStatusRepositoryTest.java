package com.system.facede.repository;

import com.system.facede.dto.NotificationStatusReportDTO;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class NotificationStatusRepositoryTest {

    @Autowired
    private NotificationStatusRepository notificationStatusRepository;

    @Autowired
    private CustomUserRepository customUserRepository;

    private CustomUser user;

    @BeforeEach
    void setUp() {
        notificationStatusRepository.deleteAll(); // just clear statuses
        // DO NOT delete customUserRepository here

        user = new CustomUser();
        user.setName("Alice");
        user.setEmail("alice@test.com");
        user.setPhoneNumber("123456");
        user = customUserRepository.save(user); // make sure it's the same user

        // Save notification statuses via setters

        NotificationStatus emailDelivered = new NotificationStatus();
        emailDelivered.setCustomUser(user);
        emailDelivered.setChannel("EMAIL");
        emailDelivered.setStatus("DELIVERED");
        notificationStatusRepository.save(emailDelivered);

        NotificationStatus emailFailed = new NotificationStatus();
        emailFailed.setCustomUser(user);
        emailFailed.setChannel("EMAIL");
        emailFailed.setStatus("FAILED");
        notificationStatusRepository.save(emailFailed);

        NotificationStatus smsPending = new NotificationStatus();
        smsPending.setCustomUser(user);
        smsPending.setChannel("SMS");
        smsPending.setStatus("PENDING");
        notificationStatusRepository.save(smsPending);

        NotificationStatus postalDelivered = new NotificationStatus();
        postalDelivered.setCustomUser(user);
        postalDelivered.setChannel("POSTAL");
        postalDelivered.setStatus("DELIVERED");
        notificationStatusRepository.save(postalDelivered);
    }

    @Test
    void findByCustomUserId_shouldReturnUserStatuses() {
        List<NotificationStatus> results = notificationStatusRepository.findByCustomUserId(user.getId());

        assertEquals(4, results.size());
        assertTrue(results.stream().allMatch(ns -> ns.getCustomUser().getId().equals(user.getId())));
    }

    @Test
    void getNotificationStatusCounts_shouldAggregateCorrectly() {
        NotificationStatusReportDTO report = notificationStatusRepository.getNotificationStatusCounts();

        assertEquals(1L, report.getDeliveredSmsCount());  // 0
        assertEquals(0L, report.getFailedSmsCount());     // 0
        assertEquals(1L, report.getPendingSmsCount());    // 1

        assertEquals(1L, report.getDeliveredEmailCount()); // 1
        assertEquals(1L, report.getFailedEmailCount());    // 1
        assertEquals(0L, report.getPendingEmailCount());   // 0

        assertEquals(1L, report.getDeliveredPostalCount()); // 1
        assertEquals(0L, report.getPendingPostalCount());    // 0
        assertEquals(0L, report.getPendingPostalCount());   // 0
    }
}
