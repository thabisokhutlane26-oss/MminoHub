package com.mminohub.app;

public class MusicPurchase {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PAID = "PAID";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_REFUNDED = "REFUNDED";

    private String id;
    private String userId;
    private String trackId;
    private String artistId;
    private double amount;
    private String currency;
    private String paymentReference;
    private String paymentMethod;
    private String status;
    private long purchaseDate;

    public MusicPurchase(
            String id,
            String userId,
            String trackId,
            String artistId,
            double amount,
            String currency,
            String paymentMethod,
            String paymentReference
    ) {
        this.id = id;
        this.userId = userId;
        this.trackId = trackId;
        this.artistId = artistId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
        this.status = STATUS_PENDING;
        this.purchaseDate = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getArtistId() {
        return artistId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public boolean isPaid() {
        return STATUS_PAID.equals(status);
    }

    public void markAsPaid(String reference) {
        if (reference != null && !reference.trim().isEmpty()) {
            paymentReference = reference;
            status = STATUS_PAID;
        }
    }

    public void markAsFailed() {
        status = STATUS_FAILED;
    }

    public void markAsRefunded() {
        if (isPaid()) {
            status = STATUS_REFUNDED;
        }
    }
}