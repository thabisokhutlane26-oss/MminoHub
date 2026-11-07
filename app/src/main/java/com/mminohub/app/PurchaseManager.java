package com.mminohub.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PurchaseManager {

    private final List<MusicPurchase> purchases =
            new ArrayList<>();

    public synchronized boolean addPurchase(
            MusicPurchase purchase
    ) {
        if (purchase == null || purchase.getId() == null) {
            return false;
        }

        for (MusicPurchase existing : purchases) {
            if (existing.getId().equals(purchase.getId())) {
                return false;
            }
        }

        purchases.add(purchase);
        return true;
    }

    public synchronized MusicPurchase findPurchase(String purchaseId) {
        if (purchaseId == null) return null;

        for (MusicPurchase purchase : purchases) {
            if (purchaseId.equals(purchase.getId())) {
                return purchase;
            }
        }

        return null;
    }

    public synchronized List<MusicPurchase> getUserPurchases(
            String userId
    ) {
        List<MusicPurchase> result = new ArrayList<>();

        if (userId == null) return result;

        for (MusicPurchase purchase : purchases) {
            if (userId.equals(purchase.getUserId())) {
                result.add(purchase);
            }
        }

        return Collections.unmodifiableList(result);
    }

    public synchronized List<MusicPurchase> getPaidPurchases() {
        List<MusicPurchase> result = new ArrayList<>();

        for (MusicPurchase purchase : purchases) {
            if (purchase.isPaid()) {
                result.add(purchase);
            }
        }

        return Collections.unmodifiableList(result);
    }

    public synchronized int getPurchaseCount() {
        return purchases.size();
    }

    public synchronized int getPaidPurchaseCount() {
        int count = 0;

        for (MusicPurchase purchase : purchases) {
            if (purchase.isPaid()) {
                count++;
            }
        }

        return count;
    }

    public synchronized boolean hasPurchasedTrack(
            String userId,
            String trackId
    ) {
        if (userId == null || trackId == null) return false;

        for (MusicPurchase purchase : purchases) {
            if (userId.equals(purchase.getUserId())
                    && trackId.equals(purchase.getTrackId())
                    && purchase.isPaid()) {
                return true;
            }
        }

        return false;
    }
}