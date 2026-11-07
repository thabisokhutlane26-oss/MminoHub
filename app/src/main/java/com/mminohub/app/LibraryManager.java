package com.mminohub.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LibraryManager {

    private final Set<String> savedTrackIds = new LinkedHashSet<>();
    private final Set<String> purchasedTrackIds = new LinkedHashSet<>();
    private final Set<String> followedArtistIds = new LinkedHashSet<>();
    private final List<Playlist> playlists = new ArrayList<>();

    public synchronized boolean saveTrack(String trackId) {
        if (isInvalid(trackId)) return false;
        return savedTrackIds.add(trackId);
    }

    public synchronized boolean removeSavedTrack(String trackId) {
        return savedTrackIds.remove(trackId);
    }

    public synchronized boolean isTrackSaved(String trackId) {
        return savedTrackIds.contains(trackId);
    }

    public synchronized List<String> getSavedTrackIds() {
        return Collections.unmodifiableList(
                new ArrayList<>(savedTrackIds)
        );
    }

    public synchronized boolean addPurchasedTrack(String trackId) {
        if (isInvalid(trackId)) return false;
        return purchasedTrackIds.add(trackId);
    }

    public synchronized boolean ownsTrack(String trackId) {
        return purchasedTrackIds.contains(trackId);
    }

    public synchronized List<String> getPurchasedTrackIds() {
        return Collections.unmodifiableList(
                new ArrayList<>(purchasedTrackIds)
        );
    }

    public synchronized boolean followArtist(String artistId) {
        if (isInvalid(artistId)) return false;
        return followedArtistIds.add(artistId);
    }

    public synchronized boolean unfollowArtist(String artistId) {
        return followedArtistIds.remove(artistId);
    }

    public synchronized boolean isFollowing(String artistId) {
        return followedArtistIds.contains(artistId);
    }

    public synchronized List<String> getFollowedArtistIds() {
        return Collections.unmodifiableList(
                new ArrayList<>(followedArtistIds)
        );
    }

    public synchronized boolean addPlaylist(Playlist playlist) {
        if (playlist == null || playlist.getId() == null) {
            return false;
        }

        for (Playlist existing : playlists) {
            if (existing.getId().equals(playlist.getId())) {
                return false;
            }
        }

        playlists.add(playlist);
        return true;
    }

    public synchronized boolean removePlaylist(String playlistId) {
        if (playlistId == null) return false;

        return playlists.removeIf(
                playlist -> playlistId.equals(playlist.getId())
        );
    }

    public synchronized List<Playlist> getPlaylists() {
        return Collections.unmodifiableList(
                new ArrayList<>(playlists)
        );
    }

    public synchronized Playlist findPlaylist(String playlistId) {
        if (playlistId == null) return null;

        for (Playlist playlist : playlists) {
            if (playlistId.equals(playlist.getId())) {
                return playlist;
            }
        }

        return null;
    }

    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}