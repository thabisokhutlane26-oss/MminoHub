package com.mminohub.app;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueue {

    private final List<MusicTrack> tracks = new ArrayList<>();
    private int currentIndex = -1;
    private boolean shuffleEnabled = false;
    private boolean repeatEnabled = false;

    public void setQueue(List<MusicTrack> newTracks) {
        tracks.clear();

        if (newTracks != null) {
            for (MusicTrack track : newTracks) {
                if (track != null) {
                    tracks.add(track);
                }
            }
        }

        currentIndex = tracks.isEmpty() ? -1 : 0;
    }

    public void addTrack(MusicTrack track) {
        if (track != null) {
            tracks.add(track);

            if (currentIndex == -1) {
                currentIndex = 0;
            }
        }
    }

    public MusicTrack getCurrentTrack() {
        if (currentIndex < 0 || currentIndex >= tracks.size()) {
            return null;
        }

        return tracks.get(currentIndex);
    }

    public MusicTrack playNext() {
        if (tracks.isEmpty()) {
            return null;
        }

        if (shuffleEnabled && tracks.size() > 1) {
            int nextIndex;

            do {
                nextIndex = (int) (Math.random() * tracks.size());
            } while (nextIndex == currentIndex);

            currentIndex = nextIndex;
        } else if (currentIndex < tracks.size() - 1) {
            currentIndex++;
        } else if (repeatEnabled) {
            currentIndex = 0;
        } else {
            return null;
        }

        return getCurrentTrack();
    }

    public MusicTrack playPrevious() {
        if (tracks.isEmpty()) {
            return null;
        }

        if (currentIndex > 0) {
            currentIndex--;
        } else if (repeatEnabled) {
            currentIndex = tracks.size() - 1;
        }

        return getCurrentTrack();
    }

    public boolean playTrack(int index) {
        if (index < 0 || index >= tracks.size()) {
            return false;
        }

        currentIndex = index;
        return true;
    }

    public void removeTrack(int index) {
        if (index < 0 || index >= tracks.size()) {
            return;
        }

        tracks.remove(index);

        if (tracks.isEmpty()) {
            currentIndex = -1;
        } else if (index < currentIndex) {
            currentIndex--;
        } else if (currentIndex >= tracks.size()) {
            currentIndex = tracks.size() - 1;
        }
    }

    public List<MusicTrack> getTracks() {
        return new ArrayList<>(tracks);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getQueueSize() {
        return tracks.size();
    }

    public boolean isShuffleEnabled() {
        return shuffleEnabled;
    }

    public void setShuffleEnabled(boolean enabled) {
        shuffleEnabled = enabled;
    }

    public boolean isRepeatEnabled() {
        return repeatEnabled;
    }

    public void setRepeatEnabled(boolean enabled) {
        repeatEnabled = enabled;
    }

    public void clear() {
        tracks.clear();
        currentIndex = -1;
    }
}