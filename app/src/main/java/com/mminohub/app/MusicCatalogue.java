package com.mminohub.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MusicCatalogue {

    private final List<MusicTrack> tracks = new ArrayList<>();
    private final List<Artist> artists = new ArrayList<>();

    public synchronized void addTrack(MusicTrack track) {
        if (track == null || track.getId() == null) return;

        for (MusicTrack existing : tracks) {
            if (track.getId().equals(existing.getId())) return;
        }

        tracks.add(track);
    }

    public synchronized void addArtist(Artist artist) {
        if (artist == null || artist.getId() == null) return;

        for (Artist existing : artists) {
            if (artist.getId().equals(existing.getId())) return;
        }

        artists.add(artist);
    }

    public synchronized List<MusicTrack> getApprovedTracks() {
        List<MusicTrack> result = new ArrayList<>();

        for (MusicTrack track : tracks) {
            if (track.isApproved()) {
                result.add(track);
            }
        }

        return result;
    }

    public synchronized List<Artist> getApprovedArtists() {
        List<Artist> result = new ArrayList<>();

        for (Artist artist : artists) {
            if (artist.isApproved()) {
                result.add(artist);
            }
        }

        return result;
    }

    public synchronized MusicTrack findTrackById(String id) {
        if (id == null) return null;

        for (MusicTrack track : tracks) {
            if (id.equals(track.getId())) {
                return track;
            }
        }

        return null;
    }

    public synchronized Artist findArtistById(String id) {
        if (id == null) return null;

        for (Artist artist : artists) {
            if (id.equals(artist.getId())) {
                return artist;
            }
        }

        return null;
    }

    public synchronized List<MusicTrack> searchTracks(String query) {
        List<MusicTrack> result = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            return result;
        }

        String search = query.trim().toLowerCase(Locale.ROOT);

        for (MusicTrack track : tracks) {
            if (!track.isApproved()) continue;

            String title = track.getTitle() == null
                    ? "" : track.getTitle().toLowerCase(Locale.ROOT);

            String artist = track.getArtist() == null
                    ? "" : track.getArtist().toLowerCase(Locale.ROOT);

            String album = track.getAlbum() == null
                    ? "" : track.getAlbum().toLowerCase(Locale.ROOT);

            if (title.contains(search)
                    || artist.contains(search)
                    || album.contains(search)) {
                result.add(track);
            }
        }

        return result;
    }

    public synchronized List<MusicTrack> getTracksByArtist(String artistName) {
        List<MusicTrack> result = new ArrayList<>();

        if (artistName == null) return result;

        for (MusicTrack track : tracks) {
            if (track.isApproved()
                    && track.getArtist() != null
                    && track.getArtist().equalsIgnoreCase(artistName)) {
                result.add(track);
            }
        }

        return result;
    }

    public synchronized int getTrackCount() {
        return tracks.size();
    }

    public synchronized int getArtistCount() {
        return artists.size();
    }

    public synchronized void clearCatalogue() {
        tracks.clear();
        artists.clear();
    }
}