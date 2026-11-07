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
    private boolean loading = false;
    private boolean released = false;

    private int playbackGeneration = 0;

    private final Runnable progressUpdater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer == null || !prepared || released) {
                return;
            }

            try {
                if (mediaPlayer.isPlaying()) {
                    if (listener != null) {
                        listener.onProgressChanged(
                                mediaPlayer.getCurrentPosition(),
                                mediaPlayer.getDuration()
                        );
                    }

                    handler.postDelayed(this, 1000);
                }
            } catch (IllegalStateException e) {
                releasePlayer();
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

        if (track == null) {
            notifyError("No track selected.");
            return;
        }

        String audioUrl = track.getAudioUrl();

        if (audioUrl == null || audioUrl.trim().isEmpty()) {
            notifyError("Audio is not available for this track.");
            return;
        }

        releasePlayer();

        final int generation = playbackGeneration;

        try {
            MediaPlayer newPlayer = new MediaPlayer();
            mediaPlayer = newPlayer;
            loading = true;

            newPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
            );

            newPlayer.setOnPreparedListener(player -> {
                if (released
                        || generation != playbackGeneration
                        || player != mediaPlayer) {
                    player.release();
                    return;
                }

                loading = false;
                prepared = true;

                try {
                    player.start();

                    if (listener != null) {
                        listener.onPlaybackStarted(track);
                    }

                    handler.removeCallbacks(progressUpdater);
                    handler.post(progressUpdater);

                } catch (IllegalStateException e) {
                    notifyError("Unable to start this audio.");
                    releasePlayer();
                }
            });

            newPlayer.setOnCompletionListener(player -> {
                if (player != mediaPlayer || released) {
                    return;
                }

                handler.removeCallbacks(progressUpdater);
                prepared = false;
                loading = false;

                MusicTrack next = queue.playNext();

                if (next != null) {
                    playCurrentTrack();
                } else {
                    releasePlayer();

                    if (listener != null) {
                        listener.onPlaybackCompleted();
                    }
                }
            });

            newPlayer.setOnErrorListener((player, what, extra) -> {
                if (player == mediaPlayer) {
                    notifyError("Unable to play this audio.");
                    releasePlayer();
                }

                return true;
            });

            newPlayer.setDataSource(audioUrl);
            newPlayer.prepareAsync();

        } catch (IOException | IllegalArgumentException
                 | IllegalStateException e) {

            notifyError("Could not load this audio.");
            releasePlayer();
        }
    }

    public void pause() {
        if (mediaPlayer == null || !prepared) {
            return;
        }

        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                handler.removeCallbacks(progressUpdater);

                if (listener != null) {
                    listener.onPlaybackPaused();
                }
            }
        } catch (IllegalStateException e) {
            releasePlayer();
        }
    }

    public void resume() {
        if (released || mediaPlayer == null || !prepared) {
            return;
        }

        try {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                handler.removeCallbacks(progressUpdater);
                handler.post(progressUpdater);

                if (listener != null) {
                    listener.onPlaybackStarted(queue.getCurrentTrack());
                }
            }
        } catch (IllegalStateException e) {
            notifyError("Unable to resume playback.");
            releasePlayer();
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
        if (released) {
            return;
        }

        MusicTrack next = queue.playNext();

        if (next != null) {
            playCurrentTrack();
        }
    }

    public void playPrevious() {
        if (released) {
            return;
        }

        MusicTrack previous = queue.playPrevious();

        if (previous != null) {
            playCurrentTrack();
        }
    }

    public void seekTo(int positionMs) {
        if (mediaPlayer == null || !prepared) {
            return;
        }

        try {
            int duration = mediaPlayer.getDuration();

            int safePosition = Math.max(
                    0,
                    Math.min(positionMs, duration)
            );

            mediaPlayer.seekTo(safePosition);

            if (listener != null) {
                listener.onProgressChanged(
                        safePosition,
                        duration
                );
            }

        } catch (IllegalStateException ignored) {
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer == null || !prepared || released) {
            return false;
        }

        try {
            return mediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public boolean isPrepared() {
        return prepared;
    }

    public boolean isLoading() {
        return loading;
    }

    public int getCurrentPosition() {
        if (mediaPlayer == null || !prepared) {
            return 0;
        }

        try {
            return mediaPlayer.getCurrentPosition();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    public int getDuration() {
        if (mediaPlayer == null || !prepared) {
            return 0;
        }

        try {
            return mediaPlayer.getDuration();
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    private void notifyError(String message) {
        if (listener != null) {
            listener.onPlaybackError(message);
        }
    }

    private void releasePlayer() {
        playbackGeneration++;

        handler.removeCallbacks(progressUpdater);

        prepared = false;
        loading = false;

        if (mediaPlayer != null) {
            MediaPlayer oldPlayer = mediaPlayer;
            mediaPlayer = null;

            try {
                oldPlayer.setOnPreparedListener(null);
                oldPlayer.setOnCompletionListener(null);
                oldPlayer.setOnErrorListener(null);
                oldPlayer.reset();
            } catch (IllegalStateException ignored) {
            }

            try {
                oldPlayer.release();
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public void release() {
        released = true;
        releasePlayer();
        listener = null;
    }
}