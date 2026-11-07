package com.mminohub.app;

public class PayoutRequest {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_PAID = "PAID";
    public static final String STATUS_REJECTED = "REJECTED";

    private String id;
    private String artistId;
    private double amount;
    private String currency;
    private String paymentMethod;
    private String accountReference;
    private String status;
    private String adminNote;
    private String transactionReference;
    private long requestedAt;
    private long processedAt;

    public PayoutRequest(
            String id,
            String artistId,
            double amount,
            String currency,
            String paymentMethod,
            String accountReference
    ) {
        this.id = id;
        this.artistId = artistId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.accountReference = accountReference;
        this.status = STATUS_PENDING;
        this.requestedAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getAccountReference() {
        return accountReference;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public long getRequestedAt() {
        return requestedAt;
    }

    public long getProcessedAt() {
        return processedAt;
    }

    public void approve() {
        if (STATUS_PENDING.equals(status)) {
            status = STATUS_APPROVED;
        }
    }

    public void reject(String note) {
        if (STATUS_PENDING.equals(status)
                || STATUS_APPROVED.equals(status)) {
            status = STATUS_REJECTED;
            adminNote = note;
            processedAt = System.currentTimeMillis();
        }
    }

    public void markProcessing() {
        if (STATUS_APPROVED.equals(status)) {
            status = STATUS_PROCESSING;
        }
    }

    public void markPaid(String transactionReference) {
        if (STATUS_PROCESSING.equals(status)
                && transactionReference != null
                && !transactionReference.trim().isEmpty()) {
            this.transactionReference = transactionReference;
            status = STATUS_PAID;
            processedAt = System.currentTimeMillis();
        }
    }
}