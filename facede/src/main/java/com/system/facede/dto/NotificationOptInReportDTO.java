package com.system.facede.dto;

public class NotificationOptInReportDTO {
    private Long emailOptInCount;
    private Long smsOptInCount;
    private Long postalOptInCount;

    public NotificationOptInReportDTO(Long emailOptInCount, Long smsOptInCount, Long postalOptInCount) {
        this.emailOptInCount = emailOptInCount;
        this.smsOptInCount = smsOptInCount;
        this.postalOptInCount = postalOptInCount;
    }

    public Long getEmailOptInCount() {
        return emailOptInCount;
    }

    public void setEmailOptInCount(Long emailOptInCount) {
        this.emailOptInCount = emailOptInCount;
    }

    public Long getSmsOptInCount() {
        return smsOptInCount;
    }

    public void setSmsOptInCount(Long smsOptInCount) {
        this.smsOptInCount = smsOptInCount;
    }

    public Long getPostalOptInCount() {
        return postalOptInCount;
    }

    public void setPostalOptInCount(Long postalOptInCount) {
        this.postalOptInCount = postalOptInCount;
    }
}