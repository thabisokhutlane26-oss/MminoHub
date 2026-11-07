package com.mminohub.app;

public class Release {

    public static final String TYPE_SINGLE = "SINGLE";
    public static final String TYPE_EP = "EP";
    public static final String TYPE_ALBUM = "ALBUM";

    public static final String STATUS_DRAFT = "DRAFT";
    public static final String STATUS_SUBMITTED = "SUBMITTED";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_PUBLISHED = "PUBLISHED";

    private String id;
    private String artistId;
    private String title;
    private String description;
    private String coverUrl;
    private String type;
    private String status;
    private long releaseDate;
    private int trackCount;
    private String rejectionReason;

    public Release(
            String id,
            String artistId,
            String title,
            String description,
            String coverUrl,
            String type,
            long releaseDate
    ) {
        this.id = id;
        this.artistId = artistId;
        this.title = title;
        this.description = description;
        this.coverUrl = coverUrl;
        this.type = type;
        this.releaseDate = releaseDate;
        this.status = STATUS_DRAFT;
        this.trackCount = 0;
    }

    public String getId() {
        return id;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public boolean submitForReview() {
        if (!STATUS_DRAFT.equals(status)
                || title == null
                || title.trim().isEmpty()
                || trackCount < 1) {
            return false;
        }

        status = STATUS_SUBMITTED;
        return true;
    }

    public boolean approve() {
        if (!STATUS_SUBMITTED.equals(status)) {
            return false;
        }

        status = STATUS_APPROVED;
        return true;
    }

    public boolean reject(String reason) {
        if (!STATUS_SUBMITTED.equals(status)) {
            return false;
        }

        status = STATUS_REJECTED;
        rejectionReason = reason;
        return true;
    }

    public boolean publish() {
        if (!STATUS_APPROVED.equals(status)) {
            return false;
        }

        status = STATUS_PUBLISHED;
        return true;
    }

    public void addTrack() {
        trackCount++;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }
}