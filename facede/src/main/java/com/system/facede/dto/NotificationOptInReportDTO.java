package com.system.facede.dto;

public class NotificationOptInReportDTO {
    private long emailOptInCount;
    private long smsOptInCount;
    private long postalOptInCount;

    public NotificationOptInReportDTO(long emailOptInCount, long smsOptInCount, long postalOptInCount) {
        this.emailOptInCount = emailOptInCount;
        this.smsOptInCount = smsOptInCount;
        this.postalOptInCount = postalOptInCount;
    }

    public long getEmailOptInCount() {
        return emailOptInCount;
    }

    public void setEmailOptInCount(long emailOptInCount) {
        this.emailOptInCount = emailOptInCount;
    }

    public long getSmsOptInCount() {
        return smsOptInCount;
    }

    public void setSmsOptInCount(long smsOptInCount) {
        this.smsOptInCount = smsOptInCount;
    }

    public long getPostalOptInCount() {
        return postalOptInCount;
    }

    public void setPostalOptInCount(long postalOptInCount) {
        this.postalOptInCount = postalOptInCount;
    }
}
