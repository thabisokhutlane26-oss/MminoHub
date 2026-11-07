package com.mminohub.app;

import java.util.ArrayList;
import java.util.List;

public class Album {

    private String id;
    private String artistId;
    private String title;
    private String description;
    private String coverUrl;
    private String releaseId;
    private final List<String> trackIds = new ArrayList<>();

    public Album(
            String id,
            String artistId,
            String title,
            String description,
            String coverUrl,
            String releaseId
    ) {
        this.id = id;
        this.artistId = artistId;
        this.title = title;
        this.description = description;
        this.coverUrl = coverUrl;
        this.releaseId = releaseId;
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

    public String getReleaseId() {
        return releaseId;
    }

    public List<String> getTrackIds() {
        return new ArrayList<>(trackIds);
    }

    public boolean addTrack(String trackId) {
        if (trackId == null || trackId.trim().isEmpty()
                || trackIds.contains(trackId)) {
            return false;
        }

        trackIds.add(trackId);
        return true;
    }

    public boolean removeTrack(String trackId) {
        return trackIds.remove(trackId);
    }

    public boolean moveTrack(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= trackIds.size()
                || toIndex < 0 || toIndex >= trackIds.size()) {
            return false;
        }

        String trackId = trackIds.remove(fromIndex);
        trackIds.add(toIndex, trackId);
        return true;
    }

    public int getTrackCount() {
        return trackIds.size();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}