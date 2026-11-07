package com.mminohub.app;

import java.util.ArrayList;
import java.util.List;

public class ArtistEarningsManager {

    private final List<ArtistEarning> earnings = new ArrayList<>();

    public boolean addEarning(ArtistEarning earning) {
        if (earning == null || earning.getId() == null
                || findById(earning.getId()) != null) {
            return false;
        }

        earnings.add(earning);
        return true;
    }

    public ArtistEarning findById(String id) {
        if (id == null) {
            return null;
        }

        for (ArtistEarning earning : earnings) {
            if (id.equals(earning.getId())) {
                return earning;
            }
        }

        return null;
    }

    public List<ArtistEarning> getArtistEarnings(String artistId) {
        List<ArtistEarning> result = new ArrayList<>();

        for (ArtistEarning earning : earnings) {
            if (artistId != null && artistId.equals(earning.getArtistId())) {
                result.add(earning);
            }
        }

        return result;
    }

    public double getTotalEarnings(String artistId) {
        double total = 0;

        for (ArtistEarning earning : getArtistEarnings(artistId)) {
            total += earning.getArtistAmount();
        }

        return total;
    }

    public double getAvailableBalance(String artistId) {
        return getBalanceByStatus(artistId, ArtistEarning.STATUS_AVAILABLE);
    }

    public double getPendingBalance(String artistId) {
        return getBalanceByStatus(artistId, ArtistEarning.STATUS_PENDING);
    }

    private double getBalanceByStatus(String artistId, String status) {
        double total = 0;

        for (ArtistEarning earning : getArtistEarnings(artistId)) {
            if (status.equals(earning.getStatus())) {
                total += earning.getArtistAmount();
            }
        }

        return total;
    }

    public int getEarningCount(String artistId) {
        return getArtistEarnings(artistId).size();
    }

    public boolean removeEarning(String id) {
        ArtistEarning earning = findById(id);

        if (earning == null || ArtistEarning.STATUS_PAID.equals(earning.getStatus())) {
            return false;
        }

        return earnings.remove(earning);
    }

    public void clear() {
        earnings.clear();
    }
}