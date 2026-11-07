package com.mminohub.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class MusicPlaybackService extends Service {

    public static final String ACTION_PLAY = "com.mminohub.app.PLAY";
    public static final String ACTION_PAUSE = "com.mminohub.app.PAUSE";
    public static final String ACTION_STOP = "com.mminohub.app.STOP";

    private static final String CHANNEL_ID = "mminohub_playback";
    private static final int NOTIFICATION_ID = 1001;

    private MusicPlayerManager playerManager;

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();

        playerManager = MminoHubRepository.getInstance()
                .getMusicPlayerManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent == null ? null : intent.getAction();

        if (ACTION_PLAY.equals(action)) {
            startForeground(NOTIFICATION_ID, buildNotification());

            if (playerManager != null) {
                playerManager.resume();
            }

        } else if (ACTION_PAUSE.equals(action)) {
            if (playerManager != null) {
                playerManager.pause();
            }

            updateNotification();

        } else if (ACTION_STOP.equals(action)) {
            stopPlaybackService();
            return START_NOT_STICKY;

        } else {
            startForeground(NOTIFICATION_ID, buildNotification());
        }

        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "MminoHub Music Playback",
                    NotificationManager.IMPORTANCE_LOW
            );

            channel.setDescription("Music playback controls");

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification() {

        Intent openApp = new Intent(this, MainActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                openApp,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        Intent pauseIntent = new Intent(this, MusicPlaybackService.class);
        pauseIntent.setAction(ACTION_PAUSE);

        PendingIntent pausePendingIntent = PendingIntent.getService(
                this,
                1,
                pauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );

        String trackTitle = "MminoHub Music";
        String artistName = "Basotho Music";

        MusicTrack track = MminoHubRepository.getInstance()
                .getPlaybackQueue()
                .getCurrentTrack();

        if (track != null) {
            trackTitle = track.getTitle();
            artistName = track.getArtist();
        }

        Notification.Builder builder =
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? new Notification.Builder(this, CHANNEL_ID)
                        : new Notification.Builder(this);

        return builder
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(trackTitle)
                .setContentText(artistName)
                .setContentIntent(contentIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .addAction(
                        android.R.drawable.ic_media_pause,
                        "Pause",
                        pausePendingIntent
                )
                .setOngoing(true)
                .build();
    }

    private void updateNotification() {
        NotificationManager manager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (manager != null) {
            manager.notify(NOTIFICATION_ID, buildNotification());
        }
    }

    private void stopPlaybackService() {
        if (playerManager != null) {
            playerManager.pause();
        }

        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}