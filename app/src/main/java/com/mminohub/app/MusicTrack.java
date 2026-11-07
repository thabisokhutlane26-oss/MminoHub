package com.mminohub.app;

public class MusicTrack {

    private String id;
    private String title;
    private String artist;
    private String album;
    private String coverUrl;
    private String audioUrl;
    private String duration;
    private String category;
    private boolean premium;
    private boolean approved;

    public MusicTrack(
            String id,
            String title,
            String artist,
            String album,
            String coverUrl,
            String audioUrl,
            String duration,
            String category,
            boolean premium,
            boolean approved
    ) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.coverUrl = coverUrl;
        this.audioUrl = audioUrl;
        this.duration = duration;
        this.category = category;
        this.premium = premium;
        this.approved = approved;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getDuration() {
        return duration;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPremium() {
        return premium;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}