package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ListenerFollowingActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildScreen();
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(30));
        scroll.addView(root);

        TextView back = new TextView(this);
        back.setText("←  BACK");
        back.setTextColor(GOLD);
        back.setTextSize(14);
        back.setTypeface(null, Typeface.BOLD);
        back.setPadding(0, 0, 0, dp(24));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        TextView title = new TextView(this);
        title.setText("Artists I Follow");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Keep up with the Basotho artists you love."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        LinearLayout summary = createCard();

        TextView heading = new TextView(this);
        heading.setText("YOUR FOLLOWING");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        summary.addView(heading);

        TextView count = new TextView(this);
        count.setText("0 artists");
        count.setTextColor(WHITE);
        count.setTextSize(25);
        count.setTypeface(null, Typeface.BOLD);
        count.setPadding(0, dp(8), 0, dp(4));
        summary.addView(count);

        TextView description = new TextView(this);
        description.setText(
                "Artists you follow will appear here."
        );
        description.setTextColor(MUTED);
        description.setTextSize(12);
        summary.addView(description);

        root.addView(summary);

        TextView section = new TextView(this);
        section.setText("FOLLOWED ARTISTS");
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams sectionParams =
                new LinearLayout.LayoutParams(-1, -2);
        sectionParams.topMargin = dp(25);
        sectionParams.bottomMargin = dp(12);
        root.addView(section, sectionParams);

        LinearLayout emptyCard = createCard();

        TextView emptyIcon = new TextView(this);
        emptyIcon.setText("♫");
        emptyIcon.setTextColor(GOLD);
        emptyIcon.setTextSize(34);
        emptyIcon.setGravity(Gravity.CENTER);
        emptyCard.addView(emptyIcon);

        TextView emptyTitle = new TextView(this);
        emptyTitle.setText("You are not following anyone yet");
        emptyTitle.setTextColor(WHITE);
        emptyTitle.setTextSize(16);
        emptyTitle.setTypeface(null, Typeface.BOLD);
        emptyTitle.setGravity(Gravity.CENTER);
        emptyTitle.setPadding(0, dp(10), 0, dp(7));
        emptyCard.addView(emptyTitle);

        TextView emptyMessage = new TextView(this);
        emptyMessage.setText(
                "Discover Basotho artists and follow their profiles " +
                "to keep their music close."
        );
        emptyMessage.setTextColor(MUTED);
        emptyMessage.setTextSize(12);
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(dp(5), 0, dp(5), dp(5));
        emptyCard.addView(emptyMessage);

        root.addView(emptyCard);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • SUPPORT BASOTHO TALENT");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(12);
        card.setLayoutParams(params);

        return card;
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
