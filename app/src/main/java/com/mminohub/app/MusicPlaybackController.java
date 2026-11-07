package com.mminohub.app;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MusicPlaybackController {

    private final Context context;
    private final MminoHubRepository repository;

    public MusicPlaybackController(Context context) {
        this.context = context.getApplicationContext();
        this.repository = MminoHubRepository.getInstance();
    }

    public void playTrack(int index) {
        PlaybackQueue queue = repository.getPlaybackQueue();

        if (!queue.playTrack(index)) {
            return;
        }

        repository.getMusicPlayerManager(context).playCurrentTrack();
        startService(MusicPlaybackService.ACTION_PLAY);
    }

    public void pause() {
        repository.getMusicPlayerManager(context).pause();
        startService(MusicPlaybackService.ACTION_PAUSE);
    }

    public void resume() {
        repository.getMusicPlayerManager(context).resume();
        startService(MusicPlaybackService.ACTION_PLAY);
    }

    public void playNext() {
        repository.getMusicPlayerManager(context).playNext();
        startService(MusicPlaybackService.ACTION_PLAY);
    }

    public void playPrevious() {
        repository.getMusicPlayerManager(context).playPrevious();
        startService(MusicPlaybackService.ACTION_PLAY);
    }

    public void stop() {
        repository.getMusicPlayerManager(context).pause();

        Intent intent = new Intent(context, MusicPlaybackService.class);
        intent.setAction(MusicPlaybackService.ACTION_STOP);

        context.startService(intent);
    }

    public boolean isPlaying() {
    return repository.getMusicPlayerManager(context).isPlaying();
}

public int getDuration() {
    return repository.getMusicPlayerManager(context).getDuration();
}

public int getCurrentPosition() {
    return repository.getMusicPlayerManager(context).getCurrentPosition();
}

    private void startService(String action) {
        Intent intent = new Intent(context, MusicPlaybackService.class);
        intent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}