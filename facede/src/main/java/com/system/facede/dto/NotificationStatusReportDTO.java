package com.system.facede.dto;

public class NotificationStatusReportDTO {
    private Long deliveredSmsCount;
    private Long failedSmsCount;
    private Long pendingSmsCount;
    private Long deliveredEmailCount;
    private Long failedEmailCount;
    private Long pendingEmailCount;
    private Long deliveredPostalCount;
    private Long failedPostalCount;
    private Long pendingPostalCount;

    public NotificationStatusReportDTO(
            Long deliveredSmsCount, Long failedSmsCount, Long pendingSmsCount,
            Long deliveredEmailCount, Long failedEmailCount, Long pendingEmailCount,
            Long deliveredPostalCount, Long failedPostalCount, Long pendingPostalCount
    ) {
        this.deliveredSmsCount = deliveredSmsCount;
        this.failedSmsCount = failedSmsCount;
        this.pendingSmsCount = pendingSmsCount;
        this.deliveredEmailCount = deliveredEmailCount;
        this.failedEmailCount = failedEmailCount;
        this.pendingEmailCount = pendingEmailCount;
        this.deliveredPostalCount = deliveredPostalCount;
        this.failedPostalCount = failedPostalCount;
        this.pendingPostalCount = pendingPostalCount;
    }

    // Getters and setters for all fields

    public Long getDeliveredSmsCount() { return deliveredSmsCount; }
    public void setDeliveredSmsCount(Long deliveredSmsCount) { this.deliveredSmsCount = deliveredSmsCount; }

    public Long getFailedSmsCount() { return failedSmsCount; }
    public void setFailedSmsCount(Long failedSmsCount) { this.failedSmsCount = failedSmsCount; }

    public Long getPendingSmsCount() { return pendingSmsCount; }
    public void setPendingSmsCount(Long pendingSmsCount) { this.pendingSmsCount = pendingSmsCount; }

    public Long getDeliveredEmailCount() { return deliveredEmailCount; }
    public void setDeliveredEmailCount(Long deliveredEmailCount) { this.deliveredEmailCount = deliveredEmailCount; }

    public Long getFailedEmailCount() { return failedEmailCount; }
    public void setFailedEmailCount(Long failedEmailCount) { this.failedEmailCount = failedEmailCount; }

    public Long getPendingEmailCount() { return pendingEmailCount; }
    public void setPendingEmailCount(Long pendingEmailCount) { this.pendingEmailCount = pendingEmailCount; }

    public Long getDeliveredPostalCount() { return deliveredPostalCount; }
    public void setDeliveredPostalCount(Long deliveredPostalCount) { this.deliveredPostalCount = deliveredPostalCount; }

    public Long getFailedPostalCount() { return failedPostalCount; }
    public void setFailedPostalCount(Long failedPostalCount) { this.failedPostalCount = failedPostalCount; }

    public Long getPendingPostalCount() { return pendingPostalCount; }
    public void setPendingPostalCount(Long pendingPostalCount) { this.pendingPostalCount = pendingPostalCount; }
}