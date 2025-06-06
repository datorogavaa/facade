package com.system.facede.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "custom_user_id", nullable = false)
    private CustomUser customUser;

    private boolean emailEnabled = false;
    private boolean smsEnabled = false;
    private boolean postalEnabled = false;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isPostalEnabled() {
        return postalEnabled;
    }

    public void setPostalEnabled(boolean postalEnabled) {
        this.postalEnabled = postalEnabled;
    }
}
