package com.mminohub.app;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

public class MusicPlayerManager {

    public interface PlaybackListener {
        void onPlaybackStarted(MusicTrack track);
        void onPlaybackPaused();
        void onPlaybackCompleted();
        void onPlaybackError(String message);
        void onProgressChanged(int position, int duration);
    }

    private final Context context;
    private final PlaybackQueue queue;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private MediaPlayer mediaPlayer;
    private PlaybackListener listener;
    private boolean prepared = false;
    private boolean released = false;

    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && prepared && mediaPlayer.isPlaying()) {
                if (listener != null) {
                    listener.onProgressChanged(
                            mediaPlayer.getCurrentPosition(),
                            mediaPlayer.getDuration()
                    );
                }

                handler.postDelayed(this, 1000);
            }
        }
    };

    public MusicPlayerManager(Context context, PlaybackQueue queue) {
        this.context = context.getApplicationContext();
        this.queue = queue;
    }

    public void setPlaybackListener(PlaybackListener listener) {
        this.listener = listener;
    }

    public void playTrack(int index) {
        if (released || !queue.playTrack(index)) {
            return;
        }

        playCurrentTrack();
    }

    public void playCurrentTrack() {
        if (released) {
            return;
        }

        MusicTrack track = queue.getCurrentTrack();

        if (track == null || track.getAudioUrl() == null
                || track.getAudioUrl().trim().isEmpty()) {
            notifyError("Audio is not available for this track.");
            return;
        }

        releasePlayer();

        try {
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );

            mediaPlayer.setDataSource(track.getAudioUrl());

            mediaPlayer.setOnPreparedListener(player -> {
                if (released || player != mediaPlayer) {
                    return;
                }

                prepared = true;
                player.start();

                if (listener != null) {
                    listener.onPlaybackStarted(track);
                }

                handler.removeCallbacks(progressUpdater);
                handler.post(progressUpdater);
            });

            mediaPlayer.setOnCompletionListener(player -> {
                handler.removeCallbacks(progressUpdater);

                if (queue.getCurrentTrack() != null) {
                    MusicTrack next = queue.playNext();

                    if (next != null) {
                        playCurrentTrack();
                    } else if (listener != null) {
                        listener.onPlaybackCompleted();
                    }
                }
            });

            mediaPlayer.setOnErrorListener((player, what, extra) -> {
                notifyError("Unable to play this audio.");
                releasePlayer();
                return true;
            });

            mediaPlayer.prepareAsync();

        } catch (IOException | IllegalArgumentException e) {
            notifyError("Could not load this audio.");
            releasePlayer();
        }
    }

    public void pause() {
        if (mediaPlayer != null && prepared && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handler.removeCallbacks(progressUpdater);

            if (listener != null) {
                listener.onPlaybackPaused();
            }
        }
    }

    public void resume() {
        if (mediaPlayer != null && prepared && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            handler.post(progressUpdater);

            if (listener != null) {
                listener.onPlaybackStarted(queue.getCurrentTrack());
            }
        }
    }

    public void togglePlayback() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    public void playNext() {
        MusicTrack next = queue.playNext();

        if (next != null) {
            playCurrentTrack();
        }
    }

    public void playPrevious() {
        MusicTrack previous = queue.playPrevious();

        if (previous != null) {
            playCurrentTrack();
        }
    }

    public void seekTo(int positionMs) {
        if (mediaPlayer != null && prepared) {
            int safePosition = Math.max(
                    0,
                    Math.min(positionMs, mediaPlayer.getDuration())
            );

            mediaPlayer.seekTo(safePosition);
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && prepared && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        return mediaPlayer != null && prepared
                ? mediaPlayer.getCurrentPosition() : 0;
    }

    public int getDuration() {
        return mediaPlayer != null && prepared
                ? mediaPlayer.getDuration() : 0;
    }

    private void notifyError(String message) {
        if (listener != null) {
            listener.onPlaybackError(message);
        }
    }

    private void releasePlayer() {
        handler.removeCallbacks(progressUpdater);

        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
                mediaPlayer.release();
            } catch (IllegalStateException ignored) {
            }

            mediaPlayer = null;
        }

        prepared = false;
    }

    public void release() {
        released = true;
        releasePlayer();
        listener = null;
    }
}