package com.mminohub.app;

import java.util.ArrayList;
import java.util.List;

public class AlbumManager {

    private final List<Album> albums = new ArrayList<>();

    public boolean addAlbum(Album album) {
        if (album == null || findById(album.getId()) != null) {
            return false;
        }

        albums.add(album);
        return true;
    }

    public Album findById(String id) {
        if (id == null) {
            return null;
        }

        for (Album album : albums) {
            if (id.equals(album.getId())) {
                return album;
            }
        }

        return null;
    }

    public List<Album> getAllAlbums() {
        return new ArrayList<>(albums);
    }

    public List<Album> getArtistAlbums(String artistId) {
        List<Album> result = new ArrayList<>();

        for (Album album : albums) {
            if (artistId != null && artistId.equals(album.getArtistId())) {
                result.add(album);
            }
        }

        return result;
    }

    public boolean removeAlbum(String id) {
        Album album = findById(id);

        if (album == null) {
            return false;
        }

        return albums.remove(album);
    }

    public int getAlbumCount() {
        return albums.size();
    }

    public void clear() {
        albums.clear();
    }
}