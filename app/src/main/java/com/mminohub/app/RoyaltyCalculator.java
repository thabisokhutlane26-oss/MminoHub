package com.mminohub.app;

public class RoyaltyCalculator {

    private static final double ARTIST_SHARE = 0.80;
    private static final double PLATFORM_SHARE = 0.20;

    public static class RoyaltySplit {
        private final double grossAmount;
        private final double artistAmount;
        private final double platformAmount;
        private final String currency;

        public RoyaltySplit(
                double grossAmount,
                double artistAmount,
                double platformAmount,
                String currency
        ) {
            this.grossAmount = grossAmount;
            this.artistAmount = artistAmount;
            this.platformAmount = platformAmount;
            this.currency = currency;
        }

        public double getGrossAmount() {
            return grossAmount;
        }

        public double getArtistAmount() {
            return artistAmount;
        }

        public double getPlatformAmount() {
            return platformAmount;
        }

        public String getCurrency() {
            return currency;
        }
    }

    public static RoyaltySplit calculate(
            double grossAmount,
            String currency
    ) {
        if (grossAmount < 0) {
            throw new IllegalArgumentException(
                    "Amount cannot be negative."
            );
        }

        double artistAmount = round(grossAmount * ARTIST_SHARE);
        double platformAmount = round(grossAmount - artistAmount);

        return new RoyaltySplit(
                round(grossAmount),
                artistAmount,
                platformAmount,
                currency
        );
    }

    private static double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }

    public static double getArtistSharePercentage() {
        return ARTIST_SHARE * 100;
    }

    public static double getPlatformSharePercentage() {
        return PLATFORM_SHARE * 100;
    }
}