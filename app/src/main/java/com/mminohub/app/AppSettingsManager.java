package com.mminohub.app;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettingsManager {

    private static final String PREFS_NAME = "mminohub_settings";

    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_DARK_MODE = "dark_mode";
    private static final String KEY_AUTOPLAY = "autoplay";
    private static final String KEY_MOBILE_DATA = "mobile_data";

    private final SharedPreferences preferences;

    public AppSettingsManager(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public AppSettings loadSettings() {
        AppSettings settings = new AppSettings();

        settings.setLanguage(preferences.getString(
                KEY_LANGUAGE, AppSettings.LANGUAGE_SESOTHO));

        settings.setCurrency(preferences.getString(
                KEY_CURRENCY, AppSettings.CURRENCY_MALOTI));

        settings.setNotificationsEnabled(preferences.getBoolean(
                KEY_NOTIFICATIONS, true));

        settings.setDarkModeEnabled(preferences.getBoolean(
                KEY_DARK_MODE, true));

        settings.setAutoplayEnabled(preferences.getBoolean(
                KEY_AUTOPLAY, false));

        settings.setMobileDataPlaybackEnabled(preferences.getBoolean(
                KEY_MOBILE_DATA, true));

        return settings;
    }

    public void saveSettings(AppSettings settings) {
        if (settings == null) {
            return;
        }

        preferences.edit()
                .putString(KEY_LANGUAGE, settings.getLanguage())
                .putString(KEY_CURRENCY, settings.getCurrency())
                .putBoolean(KEY_NOTIFICATIONS,
                        settings.areNotificationsEnabled())
                .putBoolean(KEY_DARK_MODE,
                        settings.isDarkModeEnabled())
                .putBoolean(KEY_AUTOPLAY,
                        settings.isAutoplayEnabled())
                .putBoolean(KEY_MOBILE_DATA,
                        settings.isMobileDataPlaybackEnabled())
                .apply();
    }

    public void resetSettings() {
        preferences.edit().clear().apply();
    }
}