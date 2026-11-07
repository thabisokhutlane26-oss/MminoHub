package com.mminohub.app;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FollowManager {

    private final Set<String> following = new LinkedHashSet<>();

    public synchronized boolean follow(String artistId) {
        if (isInvalid(artistId)) return false;
        return following.add(artistId);
    }

    public synchronized boolean unfollow(String artistId) {
        return following.remove(artistId);
    }

    public synchronized boolean isFollowing(String artistId) {
        return following.contains(artistId);
    }

    public synchronized List<String> getFollowing() {
        return Collections.unmodifiableList(
                new ArrayList<>(following)
        );
    }

    public synchronized int getFollowingCount() {
        return following.size();
    }

    public synchronized void clearFollowing() {
        following.clear();
    }

    private boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }
}