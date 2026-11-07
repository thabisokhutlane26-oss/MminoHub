package com.mminohub.app;

import android.app.Application;

public class MminoHubApplication extends Application {

    private static MminoHubApplication instance;

    private AppSettingsManager settingsManager;
    private AppSettings appSettings;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        settingsManager = new AppSettingsManager(this);
        appSettings = settingsManager.loadSettings();
    }

    public static MminoHubApplication getInstance() {
        return instance;
    }

    public AppSettingsManager getSettingsManager() {
        return settingsManager;
    }

    public AppSettings getAppSettings() {
        return appSettings;
    }

    public void updateSettings(AppSettings settings) {
        if (settings == null) {
            return;
        }

        appSettings = settings;
        settingsManager.saveSettings(settings);
    }
}