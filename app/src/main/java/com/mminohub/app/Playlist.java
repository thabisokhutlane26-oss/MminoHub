package com.mminohub.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Playlist {

    private String id;
    private String name;
    private String ownerId;
    private String description;
    private String coverUrl;
    private final List<String> trackIds;
    private long createdAt;
    private boolean publicPlaylist;

    public Playlist(
            String id,
            String name,
            String ownerId,
            String description,
            String coverUrl,
            boolean publicPlaylist
    ) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.description = description;
        this.coverUrl = coverUrl;
        this.publicPlaylist = publicPlaylist;
        this.trackIds = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public boolean isPublicPlaylist() {
        return publicPlaylist;
    }

    public List<String> getTrackIds() {
        return Collections.unmodifiableList(
                new ArrayList<>(trackIds)
        );
    }

    public int getTrackCount() {
        return trackIds.size();
    }

    public boolean addTrack(String trackId) {
        if (trackId == null || trackId.trim().isEmpty()) {
            return false;
        }

        if (trackIds.contains(trackId)) {
            return false;
        }

        trackIds.add(trackId);
        return true;
    }

    public boolean removeTrack(String trackId) {
        return trackIds.remove(trackId);
    }

    public boolean containsTrack(String trackId) {
        return trackIds.contains(trackId);
    }

    public void moveTrack(int fromIndex, int toIndex) {
        if (fromIndex < 0 || fromIndex >= trackIds.size()) return;
        if (toIndex < 0 || toIndex >= trackIds.size()) return;

        String trackId = trackIds.remove(fromIndex);
        trackIds.add(toIndex, trackId);
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setPublicPlaylist(boolean publicPlaylist) {
        this.publicPlaylist = publicPlaylist;
    }
}