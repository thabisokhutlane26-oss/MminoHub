package com.mminohub.app;

public class ArtistEarning {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_PAID = "PAID";

    private String id;
    private String artistId;
    private String trackId;
    private String source;
    private double grossAmount;
    private double artistAmount;
    private double platformAmount;
    private String currency;
    private String status;
    private long createdAt;

    public ArtistEarning(
            String id,
            String artistId,
            String trackId,
            String source,
            double grossAmount,
            double artistAmount,
            double platformAmount,
            String currency
    ) {
        this.id = id;
        this.artistId = artistId;
        this.trackId = trackId;
        this.source = source;
        this.grossAmount = grossAmount;
        this.artistAmount = artistAmount;
        this.platformAmount = platformAmount;
        this.currency = currency;
        this.status = STATUS_PENDING;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getSource() {
        return source;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public double getArtistAmount() {
        return artistAmount;
    }

    public double getPlatformAmount() {
        return platformAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setStatus(String status) {
        if (STATUS_PENDING.equals(status)
                || STATUS_AVAILABLE.equals(status)
                || STATUS_PAID.equals(status)) {
            this.status = status;
        }
    }

    public boolean isPaid() {
        return STATUS_PAID.equals(status);
    }
}