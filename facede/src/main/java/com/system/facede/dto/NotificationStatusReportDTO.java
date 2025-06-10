package com.system.facede.dto;

public class NotificationStatusReportDTO {
    private Long deliveredCount;
    private Long failedCount;
    private Long pendingCount;

    public NotificationStatusReportDTO(Long deliveredCount, Long failedCount, Long pendingCount) {
        this.deliveredCount = deliveredCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
    }

    public Long getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(Long deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public Long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Long failedCount) {
        this.failedCount = failedCount;
    }

    public Long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(Long pendingCount) {
        this.pendingCount = pendingCount;
    }
}
