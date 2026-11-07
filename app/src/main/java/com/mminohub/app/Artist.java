package com.mminohub.app;

public class Artist {

    private String id;
    private String name;
    private String biography;
    private String profileImageUrl;
    private String location;
    private String genre;
    private int followerCount;
    private boolean verified;
    private boolean approved;

    public Artist(
            String id,
            String name,
            String biography,
            String profileImageUrl,
            String location,
            String genre,
            int followerCount,
            boolean verified,
            boolean approved
    ) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.profileImageUrl = profileImageUrl;
        this.location = location;
        this.genre = genre;
        this.followerCount = followerCount;
        this.verified = verified;
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getLocation() {
        return location;
    }

    public String getGenre() {
        return genre;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public boolean isVerified() {
        return verified;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = Math.max(0, followerCount);
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}