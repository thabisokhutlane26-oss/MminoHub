package com.mminohub.app;

public class UserProfile {

    public static final String ROLE_LISTENER = "LISTENER";
    public static final String ROLE_ARTIST = "ARTIST";
    public static final String ROLE_ADMIN = "ADMIN";

    private String id;
    private String displayName;
    private String email;
    private String profileImageUrl;
    private String country;
    private String role;
    private boolean premium;
    private boolean active;
    private long createdAt;

    public UserProfile(
            String id,
            String displayName,
            String email,
            String profileImageUrl,
            String country,
            String role
    ) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.country = country;
        this.role = role;
        this.premium = false;
        this.active = true;
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getCountry() {
        return country;
    }

    public String getRole() {
        return role;
    }

    public boolean isPremium() {
        return premium;
    }

    public boolean isActive() {
        return active;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isArtist() {
        return ROLE_ARTIST.equals(role);
    }

    public boolean isAdmin() {
        return ROLE_ADMIN.equals(role);
    }

    public void setDisplayName(String displayName) {
        if (displayName != null && !displayName.trim().isEmpty()) {
            this.displayName = displayName.trim();
        }
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setRole(String role) {
        if (ROLE_LISTENER.equals(role)
                || ROLE_ARTIST.equals(role)
                || ROLE_ADMIN.equals(role)) {
            this.role = role;
        }
    }
}