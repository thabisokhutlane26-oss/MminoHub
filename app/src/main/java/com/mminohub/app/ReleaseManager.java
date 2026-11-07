package com.mminohub.app;

import java.util.ArrayList;
import java.util.List;

public class ReleaseManager {

    private final List<Release> releases = new ArrayList<>();

    public void addRelease(Release release) {
        if (release == null || findById(release.getId()) != null) {
            return;
        }

        releases.add(release);
    }

    public Release findById(String id) {
        for (Release release : releases) {
            if (release.getId().equals(id)) {
                return release;
            }
        }
        return null;
    }

    public List<Release> getAllReleases() {
        return new ArrayList<>(releases);
    }

    public List<Release> getArtistReleases(String artistId) {
        List<Release> result = new ArrayList<>();

        for (Release release : releases) {
            if (release.getArtistId().equals(artistId)) {
                result.add(release);
            }
        }

        return result;
    }

    public List<Release> getReleasesByStatus(String status) {
        List<Release> result = new ArrayList<>();

        for (Release release : releases) {
            if (release.getStatus().equals(status)) {
                result.add(release);
            }
        }

        return result;
    }

    public List<Release> getPublishedReleases() {
        return getReleasesByStatus(Release.STATUS_PUBLISHED);
    }

    public List<Release> getPendingReviews() {
        return getReleasesByStatus(Release.STATUS_SUBMITTED);
    }

    public boolean removeRelease(String id) {
        Release release = findById(id);

        if (release == null || Release.STATUS_PUBLISHED.equals(release.getStatus())) {
            return false;
        }

        return releases.remove(release);
    }

    public int getReleaseCount() {
        return releases.size();
    }

    public void clear() {
        releases.clear();
    }
}