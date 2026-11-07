package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;
import android.os.Looper;

public class MusicPlayerActivity extends Activity {

    private final int GOLD = Color.rgb(225, 185, 90);
    private final int BACKGROUND = Color.rgb(7, 11, 18);
    private final int CARD = Color.rgb(15, 22, 33);
    private final int WHITE = Color.WHITE;

    private MusicPlaybackController controller;

    private TextView titleView;
    private TextView artistView;
    private TextView playButton;
    private TextView currentTime;
    private TextView durationView;
    private TextView premiumLabel;

    private SeekBar seekBar;

    private boolean userSeeking = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Runnable updater = new Runnable() {
        @Override
        public void run() {
            refreshPlayer();

            if (!isFinishing()) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller = new MusicPlaybackController(this);

        buildPlayer();
        refreshPlayer();

        handler.post(updater);
    }

    private void buildPlayer() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(22), dp(18), dp(22), dp(18));
        root.setBackgroundColor(BACKGROUND);

        TextView back = text("‹", 34, GOLD, false);
        back.setGravity(Gravity.CENTER_VERTICAL);
        back.setOnClickListener(v -> finish());

        root.addView(back, new LinearLayout.LayoutParams(
                -1, dp(45)
        ));

        TextView heading = text("NOW PLAYING", 12, GOLD, true);
        heading.setGravity(Gravity.CENTER);
        root.addView(heading);

        TextView cover = text("♪", 100, GOLD, true);
        cover.setGravity(Gravity.CENTER);

        GradientDrawable coverBackground = new GradientDrawable();
        coverBackground.setColor(CARD);
        coverBackground.setCornerRadius(dp(22));
        coverBackground.setStroke(dp(1), GOLD);
        cover.setBackground(coverBackground);

        LinearLayout.LayoutParams coverParams =
                new LinearLayout.LayoutParams(
                        -1, dp(270)
                );

        coverParams.setMargins(0, dp(28), 0, dp(25));
        root.addView(cover, coverParams);

        titleView = text("Nothing playing", 22, WHITE, true);
        titleView.setGravity(Gravity.CENTER);
        root.addView(titleView);

        artistView = text("Discover Basotho music", 15,
                Color.LTGRAY, false);
        artistView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams artistParams =
                new LinearLayout.LayoutParams(
                        -1, dp(35)
                );

        root.addView(artistView, artistParams);

        premiumLabel = text("", 11, GOLD, true);
        premiumLabel.setGravity(Gravity.CENTER);
        root.addView(premiumLabel);

        seekBar = new SeekBar(this);
        seekBar.setMax(1000);
        seekBar.setProgressTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );
        seekBar.setThumbTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );

        LinearLayout.LayoutParams seekParams =
                new LinearLayout.LayoutParams(-1, dp(35));

        seekParams.setMargins(0, dp(18), 0, 0);
        root.addView(seekBar, seekParams);

        LinearLayout times = new LinearLayout(this);
        times.setOrientation(LinearLayout.HORIZONTAL);

        currentTime = text("0:00", 11, Color.LTGRAY, false);
        durationView = text("0:00", 11, Color.LTGRAY, false);

        times.addView(currentTime, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        times.addView(durationView);

        root.addView(times);

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(
                            SeekBar bar,
                            int progress,
                            boolean fromUser
                    ) {
                        if (fromUser) {
                            currentTime.setText(formatTime(
                                    progress * controller.getDuration() / 1000
                            ));
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar bar) {
                        userSeeking = true;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar bar) {
                        userSeeking = false;

                        int position =
                                bar.getProgress()
                                        * controller.getDuration()
                                        / 1000;

                        MminoHubRepository.getInstance()
                                .getMusicPlayerManager(
                                        MusicPlayerActivity.this
                                )
                                .seekTo(position);
                    }
                }
        );

        LinearLayout controls = new LinearLayout(this);
        controls.setGravity(Gravity.CENTER);
        controls.setOrientation(LinearLayout.HORIZONTAL);

        addControl(controls, "⏮", () -> {
            controller.playPrevious();
            refreshPlayer();
        }, 45);

        playButton = addControl(controls, "▶", () -> {
            if (controller.isPlaying()) {
                controller.pause();
            } else {
                controller.resume();
            }

            refreshPlayer();
        }, 68);

        addControl(controls, "⏭", () -> {
            controller.playNext();
            refreshPlayer();
        }, 45);

        LinearLayout.LayoutParams controlParams =
                new LinearLayout.LayoutParams(
                        -1, dp(85)
                );

        controlParams.setMargins(0, dp(12), 0, dp(5));
        root.addView(controls, controlParams);

        LinearLayout extraControls = new LinearLayout(this);
        extraControls.setGravity(Gravity.CENTER);

        addControl(extraControls, "🔀", () -> {
            PlaybackQueue queue = MminoHubRepository.getInstance()
                    .getPlaybackQueue();

            queue.setShuffleEnabled(!queue.isShuffleEnabled());

            Toast.makeText(
                    this,
                    queue.isShuffleEnabled()
                            ? "Shuffle on" : "Shuffle off",
                    Toast.LENGTH_SHORT
            ).show();
        }, 48);

        addControl(extraControls, "🔁", () -> {
            PlaybackQueue queue = MminoHubRepository.getInstance()
                    .getPlaybackQueue();

            queue.setRepeatEnabled(!queue.isRepeatEnabled());

            Toast.makeText(
                    this,
                    queue.isRepeatEnabled()
                            ? "Repeat on" : "Repeat off",
                    Toast.LENGTH_SHORT
            ).show();
        }, 48);

        root.addView(extraControls);

        setContentView(root);
    }

    private TextView addControl(
            LinearLayout parent,
            String symbol,
            Runnable action,
            int size
    ) {
        TextView button = text(symbol, 25, GOLD, true);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(v -> action.run());

        parent.addView(button, new LinearLayout.LayoutParams(
                dp(size), dp(65)
        ));

        return button;
    }

    private void refreshPlayer() {

        MusicPlayerManager manager =
                MminoHubRepository.getInstance()
                        .getMusicPlayerManager(this);

        MusicTrack track = MminoHubRepository.getInstance()
                .getPlaybackQueue()
                .getCurrentTrack();

        if (track == null) {
            titleView.setText("Nothing playing");
            artistView.setText("Discover Basotho music");
            premiumLabel.setText("");
            playButton.setText("▶");
            seekBar.setProgress(0);
            currentTime.setText("0:00");
            durationView.setText("0:00");
            return;
        }

        titleView.setText(track.getTitle());
        artistView.setText(track.getArtist());

        premiumLabel.setText(
                track.isPremium() ? "PREMIUM TRACK" : ""
        );

        playButton.setText(
                manager.isPlaying() ? "⏸" : "▶"
        );

        int duration = manager.getDuration();
        int position = manager.getCurrentPosition();

        if (!userSeeking && duration > 0) {
            seekBar.setProgress(
                    (int) ((position * 1000L) / duration)
            );
        }

        seekBar.setEnabled(duration > 0);

        currentTime.setText(formatTime(position));
        durationView.setText(formatTime(duration));
    }

    private String formatTime(int milliseconds) {
        int seconds = Math.max(0, milliseconds / 1000);

        return String.format(
                java.util.Locale.getDefault(),
                "%d:%02d",
                seconds / 60,
                seconds % 60
        );
    }

    private TextView text(
            String value,
            float size,
            int color,
            boolean bold
    ) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);

        if (bold) {
            view.setTypeface(null, Typeface.BOLD);
        }

        return view;
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(updater);
        super.onDestroy();
    }
}