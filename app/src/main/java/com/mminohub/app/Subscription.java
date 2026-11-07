package com.mminohub.app;

public class Subscription {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_EXPIRED = "EXPIRED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_PENDING = "PENDING";

    public static final String PLAN_MONTHLY = "MONTHLY";

    private String id;
    private String userId;
    private String plan;
    private String status;
    private double price;
    private String currency;
    private long startDate;
    private long expiryDate;
    private boolean autoRenew;

    public Subscription(
            String id,
            String userId,
            String plan,
            double price,
            String currency,
            long startDate,
            long expiryDate
    ) {
        this.id = id;
        this.userId = userId;
        this.plan = plan;
        this.price = price;
        this.currency = currency;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.status = STATUS_PENDING;
        this.autoRenew = false;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPlan() {
        return plan;
    }

    public String getStatus() {
        return status;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public boolean isActive() {
        return STATUS_ACTIVE.equals(status)
                && System.currentTimeMillis() < expiryDate;
    }

    public void activate() {
        status = STATUS_ACTIVE;
    }

    public void cancel() {
        status = STATUS_CANCELLED;
        autoRenew = false;
    }

    public void expire() {
        status = STATUS_EXPIRED;
        autoRenew = false;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public void setStatus(String status) {
        if (STATUS_ACTIVE.equals(status)
                || STATUS_EXPIRED.equals(status)
                || STATUS_CANCELLED.equals(status)
                || STATUS_PENDING.equals(status)) {
            this.status = status;
        }
    }
}