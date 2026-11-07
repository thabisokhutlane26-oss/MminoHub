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
    public static final String ACTION_NEXT = "com.mminohub.app.NEXT";
    public static final String ACTION_PREVIOUS = "com.mminohub.app.PREVIOUS";

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

        if (ACTION_STOP.equals(action)) {
            stopPlaybackService();
            return START_NOT_STICKY;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, buildNotification());
        }

        if (playerManager == null) {
            playerManager = MminoHubRepository.getInstance()
                    .getMusicPlayerManager(this);
        }

        if (ACTION_PLAY.equals(action)) {
            if (!playerManager.isPlaying()) {
                playerManager.resume();
            }

        } else if (ACTION_PAUSE.equals(action)) {
            playerManager.pause();

        } else if (ACTION_NEXT.equals(action)) {
            playerManager.playNext();

        } else if (ACTION_PREVIOUS.equals(action)) {
            playerManager.playPrevious();
        }

        updateNotification();

        return START_NOT_STICKY;
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

    private PendingIntent createActionIntent(String action, int requestCode) {
        Intent intent = new Intent(this, MusicPlaybackService.class);
        intent.setAction(action);

        return PendingIntent.getService(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_IMMUTABLE
        );
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

        boolean playing = playerManager != null
                && playerManager.isPlaying();

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

        builder.setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle(trackTitle)
                .setContentText(artistName)
                .setContentIntent(contentIntent)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setOngoing(playing);

        builder.addAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                createActionIntent(ACTION_PREVIOUS, 2)
        );

        builder.addAction(
                playing
                        ? android.R.drawable.ic_media_pause
                        : android.R.drawable.ic_media_play,
                playing ? "Pause" : "Play",
                createActionIntent(
                        playing ? ACTION_PAUSE : ACTION_PLAY,
                        3
                )
        );

        builder.addAction(
                android.R.drawable.ic_media_next,
                "Next",
                createActionIntent(ACTION_NEXT, 4)
        );

        builder.addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop",
                createActionIntent(ACTION_STOP, 5)
        );

        return builder.build();
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