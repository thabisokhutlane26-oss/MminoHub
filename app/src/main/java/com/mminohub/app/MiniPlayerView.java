package com.mminohub.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MiniPlayerView extends LinearLayout {

    private final TextView titleView;
    private final TextView artistView;
    private final ImageButton playButton;

    private final MusicPlaybackController controller;

    public MiniPlayerView(Context context) {
        super(context);

        controller = new MusicPlaybackController(context);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(dp(12), dp(8), dp(12), dp(8));

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
        information.setPadding(dp(10), 0, dp(6), 0);

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

        ImageButton previousButton = createButton("⏮");
        previousButton.setOnClickListener(v -> {
            controller.playPrevious();
            refresh();
        });

        addView(previousButton, new LayoutParams(dp(42), dp(42)));

        playButton = createButton("▶");
        playButton.setOnClickListener(v -> {
            if (controller.isPlaying()) {
                controller.pause();
            } else {
                controller.resume();
            }

            refresh();
        });

        addView(playButton, new LayoutParams(dp(42), dp(42)));

        ImageButton nextButton = createButton("⏭");
        nextButton.setOnClickListener(v -> {
            controller.playNext();
            refresh();
        });

        addView(nextButton, new LayoutParams(dp(42), dp(42)));

        refresh();
    }

    private ImageButton createButton(String symbol) {
        ImageButton button = new ImageButton(getContext());

        button.setImageDrawable(null);
        button.setBackgroundColor(Color.TRANSPARENT);
        button.setContentDescription(symbol);

        TextView label = new TextView(getContext());
        label.setText(symbol);
        label.setTextColor(Color.WHITE);
        label.setTextSize(18);
        label.setGravity(Gravity.CENTER);

        button.setOnClickListener(null);
        button.setTag(label);

        return createTextButton(symbol);
    }

    private ImageButton createTextButton(String symbol) {
        ImageButton button = new ImageButton(getContext());

        android.graphics.drawable.Drawable drawable =
                new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT);

        button.setBackground(drawable);
        button.setContentDescription(symbol);

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

        playButton.setImageDrawable(null);
        playButton.setContentDescription(
                controller.isPlaying() ? "Pause" : "Play"
        );

        playButton.setTag(controller.isPlaying() ? "⏸" : "▶");
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }
}