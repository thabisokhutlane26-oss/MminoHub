package com.mminohub.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MiniPlayerView extends LinearLayout {

    private final TextView titleView;
    private final TextView artistView;
    private final TextView playButton;

    private final MusicPlaybackController controller;

    public MiniPlayerView(Context context) {
        super(context);

        controller = new MusicPlaybackController(context);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(dp(10), dp(8), dp(10), dp(8));

        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.rgb(15, 22, 33));
        background.setCornerRadius(dp(14));
        background.setStroke(dp(1), Color.rgb(55, 65, 80));
        setBackground(background);

        TextView cover = new TextView(context);
        cover.setText("♪");
        cover.setTextSize(25);
        cover.setTextColor(Color.rgb(225, 185, 90));
        cover.setGravity(Gravity.CENTER);
        cover.setBackgroundColor(Color.rgb(30, 39, 53));

        addView(cover, new LayoutParams(dp(44), dp(44)));

        LinearLayout information = new LinearLayout(context);
        information.setOrientation(VERTICAL);
        information.setGravity(Gravity.CENTER_VERTICAL);
        information.setPadding(dp(10), 0, dp(4), 0);

        titleView = new TextView(context);
        titleView.setText("Nothing playing");
        titleView.setTextColor(Color.WHITE);
        titleView.setTextSize(13);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setSingleLine(true);

        artistView = new TextView(context);
        artistView.setText("Discover Basotho music");
        artistView.setTextColor(Color.LTGRAY);
        artistView.setTextSize(11);
        artistView.setSingleLine(true);

        information.addView(titleView);
        information.addView(artistView);

        addView(information, new LayoutParams(
                0,
                LayoutParams.WRAP_CONTENT,
                1
        ));

        addControl("⏮", () -> {
            controller.playPrevious();
            refresh();
        });

        playButton = addControl("▶", () -> {
            if (controller.isPlaying()) {
                controller.pause();
            } else {
                controller.resume();
            }

            refresh();
        });

        addControl("⏭", () -> {
            controller.playNext();
            refresh();
        });

        refresh();
    }

    private TextView addControl(String symbol, Runnable action) {
        TextView button = new TextView(getContext());

        button.setText(symbol);
        button.setTextColor(Color.WHITE);
        button.setTextSize(19);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(android.R.drawable.list_selector_background);
        button.setContentDescription(symbol);

        addView(button, new LayoutParams(dp(38), dp(44)));

        button.setOnClickListener(v -> action.run());

        return button;
    }

    public void refresh() {
        MusicTrack track = MminoHubRepository.getInstance()
                .getPlaybackQueue()
                .getCurrentTrack();

        if (track == null) {
            titleView.setText("Nothing playing");
            artistView.setText("Discover Basotho music");
        } else {
            titleView.setText(track.getTitle());
            artistView.setText(track.getArtist());
        }

        playButton.setText(controller.isPlaying() ? "⏸" : "▶");
        playButton.setContentDescription(
                controller.isPlaying() ? "Pause" : "Play"
        );
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }
}