package com.mminohub.app;

public class AppSettings {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_SESOTHO = "st";

    public static final String CURRENCY_MALOTI = "LSL";

    private String language = LANGUAGE_SESOTHO;
    private String currency = CURRENCY_MALOTI;
    private boolean notificationsEnabled = true;
    private boolean darkModeEnabled = true;
    private boolean autoplayEnabled = false;
    private boolean mobileDataPlaybackEnabled = true;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        if (LANGUAGE_ENGLISH.equals(language)
                || LANGUAGE_SESOTHO.equals(language)) {
            this.language = language;
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (currency != null && !currency.trim().isEmpty()) {
            this.currency = currency;
        }
    }

    public boolean areNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean enabled) {
        notificationsEnabled = enabled;
    }

    public boolean isDarkModeEnabled() {
        return darkModeEnabled;
    }

    public void setDarkModeEnabled(boolean enabled) {
        darkModeEnabled = enabled;
    }

    public boolean isAutoplayEnabled() {
        return autoplayEnabled;
    }

    public void setAutoplayEnabled(boolean enabled) {
        autoplayEnabled = enabled;
    }

    public boolean isMobileDataPlaybackEnabled() {
        return mobileDataPlaybackEnabled;
    }

    public void setMobileDataPlaybackEnabled(boolean enabled) {
        mobileDataPlaybackEnabled = enabled;
    }
}