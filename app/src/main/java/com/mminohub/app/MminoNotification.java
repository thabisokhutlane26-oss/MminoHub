package com.mminohub.app;

public class MminoNotification {

    public static final String TYPE_NEW_RELEASE = "NEW_RELEASE";
    public static final String TYPE_NEW_FOLLOWER = "NEW_FOLLOWER";
    public static final String TYPE_PURCHASE = "PURCHASE";
    public static final String TYPE_SYSTEM = "SYSTEM";

    private String id;
    private String userId;
    private String title;
    private String message;
    private String type;
    private String relatedId;
    private long createdAt;
    private boolean read;

    public MminoNotification(
            String id,
            String userId,
            String title,
            String message,
            String type,
            String relatedId
    ) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.relatedId = relatedId;
        this.createdAt = System.currentTimeMillis();
        this.read = false;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getRelatedId() {
        return relatedId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        read = true;
    }
}