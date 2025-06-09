package com.system.facede.dto;

public class NotificationStatusReportDTO {
    private long deliveredCount;
    private long failedCount;
    private long pendingCount;

    public NotificationStatusReportDTO(long deliveredCount, long failedCount, long pendingCount) {
        this.deliveredCount = deliveredCount;
        this.failedCount = failedCount;
        this.pendingCount = pendingCount;
    }

    // Getters and Setters
    public long getDeliveredCount() {
        return deliveredCount;
    }

    public void setDeliveredCount(long deliveredCount) {
        this.deliveredCount = deliveredCount;
    }

    public long getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(long failedCount) {
        this.failedCount = failedCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }
}
