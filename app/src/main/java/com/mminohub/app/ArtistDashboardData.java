package com.mminohub.app;

public class ArtistDashboardData {

    private String artistId;
    private int totalStreams;
    private int totalSales;
    private int totalFollowers;
    private double totalEarnings;
    private double availableBalance;
    private double pendingBalance;
    private String currency;

    public ArtistDashboardData(String artistId, String currency) {
        this.artistId = artistId;
        this.currency = currency;
        this.totalStreams = 0;
        this.totalSales = 0;
        this.totalFollowers = 0;
        this.totalEarnings = 0;
        this.availableBalance = 0;
        this.pendingBalance = 0;
    }

    public String getArtistId() {
        return artistId;
    }

    public int getTotalStreams() {
        return totalStreams;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public int getTotalFollowers() {
        return totalFollowers;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public double getPendingBalance() {
        return pendingBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setTotalStreams(int totalStreams) {
        this.totalStreams = Math.max(0, totalStreams);
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = Math.max(0, totalSales);
    }

    public void setTotalFollowers(int totalFollowers) {
        this.totalFollowers = Math.max(0, totalFollowers);
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = Math.max(0, totalEarnings);
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = Math.max(0, availableBalance);
    }

    public void setPendingBalance(double pendingBalance) {
        this.pendingBalance = Math.max(0, pendingBalance);
    }

    public void addStream() {
        totalStreams++;
    }

    public void addSale() {
        totalSales++;
    }

    public void addFollower() {
        totalFollowers++;
    }

    public void removeFollower() {
        if (totalFollowers > 0) {
            totalFollowers--;
        }
    }
}