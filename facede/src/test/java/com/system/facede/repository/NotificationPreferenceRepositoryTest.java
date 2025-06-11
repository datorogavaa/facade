package com.system.facede.repository;

import com.system.facede.dto.NotificationOptInReportDTO;
import com.system.facede.model.CustomUser;
import com.system.facede.model.NotificationPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class NotificationPreferenceRepositoryTest {

    @Autowired
    private NotificationPreferenceRepository preferenceRepository;

    @Autowired
    private CustomUserRepository customUserRepository;

    private CustomUser user1, user2;

    @BeforeEach
    void setUp() {
        preferenceRepository.deleteAll();
        customUserRepository.deleteAll();

        user1 = new CustomUser();
        user1.setName("User One");
        user1.setEmail("one@test.com");
        user1.setPhoneNumber("111");
        customUserRepository.save(user1);

        user2 = new CustomUser();
        user2.setName("User Two");
        user2.setEmail("two@test.com");
        user2.setPhoneNumber("222");
        customUserRepository.save(user2);

        NotificationPreference pref1 = new NotificationPreference();
        pref1.setCustomUser(user1);
        pref1.setEmailEnabled(true);
        pref1.setSmsEnabled(false);
        pref1.setPostalEnabled(true);
        preferenceRepository.save(pref1);

        NotificationPreference pref2 = new NotificationPreference();
        pref2.setCustomUser(user2);
        pref2.setEmailEnabled(false);
        pref2.setSmsEnabled(true);
        pref2.setPostalEnabled(false);
        preferenceRepository.save(pref2);
    }

    @Test
    void findByCustomUserId_shouldReturnPreference() {
        Optional<NotificationPreference> result = preferenceRepository.findByCustomUserId(user1.getId());
        assertTrue(result.isPresent());
        assertEquals(user1.getId(), result.get().getCustomUser().getId());
    }

    @Test
    void getOptInCounts_shouldReturnCorrectSums() {
        NotificationOptInReportDTO report = preferenceRepository.getOptInCounts();

        assertEquals(1L, report.getEmailOptInCount());
        assertEquals(1L, report.getSmsOptInCount());
        assertEquals(1L, report.getPostalOptInCount());
    }

    @Test
    void updatePreferencesForMultipleUsers_shouldUpdateAllFields() {
        preferenceRepository.updatePreferencesForMultipleUsers(
                List.of(user1.getId(), user2.getId()),
                true, false, false
        );

        Optional<NotificationPreference> updated1 = preferenceRepository.findByCustomUserId(user1.getId());
        Optional<NotificationPreference> updated2 = preferenceRepository.findByCustomUserId(user2.getId());

        assertTrue(updated1.isPresent());
        assertTrue(updated2.isPresent());

        assertTrue(updated1.get().isEmailEnabled());
        assertFalse(updated1.get().isSmsEnabled());
        assertFalse(updated1.get().isPostalEnabled());

        assertTrue(updated2.get().isEmailEnabled());
        assertFalse(updated2.get().isSmsEnabled());
        assertFalse(updated2.get().isPostalEnabled());
    }
}
