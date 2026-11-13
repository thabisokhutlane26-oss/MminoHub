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

public class ListenerNotificationsActivity extends Activity {

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
        title.setText("Notifications");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Important updates from your MminoHub experience."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        LinearLayout summary = createCard();

        TextView summaryTitle = new TextView(this);
        summaryTitle.setText("YOUR NOTIFICATION CENTRE");
        summaryTitle.setTextColor(GOLD);
        summaryTitle.setTextSize(11);
        summaryTitle.setTypeface(null, Typeface.BOLD);
        summary.addView(summaryTitle);

        TextView count = new TextView(this);
        count.setText("0 new notifications");
        count.setTextColor(WHITE);
        count.setTextSize(21);
        count.setTypeface(null, Typeface.BOLD);
        count.setPadding(0, dp(9), 0, dp(5));
        summary.addView(count);

        TextView description = new TextView(this);
        description.setText("You are all caught up.");
        description.setTextColor(MUTED);
        description.setTextSize(12);
        summary.addView(description);

        root.addView(summary);

        addSection(root, "UPDATES");

        addNotificationCategory(
                root, "♫", "New music",
                "Updates when followed artists release new music."
        );

        addNotificationCategory(
                root, "★", "Artist updates",
                "News and announcements from artists you follow."
        );

        addNotificationCategory(
                root, "◆", "Purchases",
                "Updates about your music purchases and orders."
        );

        addNotificationCategory(
                root, "♧", "Account and security",
                "Important information about your MminoHub account."
        );

        addSection(root, "NOTIFICATION HISTORY");

        LinearLayout emptyCard = createCard();

        TextView emptyTitle = new TextView(this);
        emptyTitle.setText("No notifications yet");
        emptyTitle.setTextColor(WHITE);
        emptyTitle.setTextSize(16);
        emptyTitle.setTypeface(null, Typeface.BOLD);
        emptyTitle.setGravity(Gravity.CENTER);
        emptyCard.addView(emptyTitle);

        TextView emptyMessage = new TextView(this);
        emptyMessage.setText(
                "When there are updates for your account, " +
                "they will appear here."
        );
        emptyMessage.setTextColor(MUTED);
        emptyMessage.setTextSize(12);
        emptyMessage.setGravity(Gravity.CENTER);
        emptyMessage.setPadding(0, dp(8), 0, 0);
        emptyCard.addView(emptyMessage);

        root.addView(emptyCard);

        TextView note = new TextView(this);
        note.setText(
                "Notification delivery and preferences will be " +
                "connected when the MminoHub notification service " +
                "is implemented."
        );
        note.setTextColor(MUTED);
        note.setTextSize(11);
        note.setPadding(dp(4), dp(16), dp(4), 0);
        root.addView(note);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • STAY CONNECTED");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addSection(LinearLayout parent, String label) {
        TextView section = new TextView(this);
        section.setText(label);
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(23);
        params.bottomMargin = dp(12);

        parent.addView(section, params);
    }

    private void addNotificationCategory(
            LinearLayout parent,
            String symbol,
            String heading,
            String description
    ) {
        LinearLayout card = createCard();
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);

        TextView icon = new TextView(this);
        icon.setText(symbol);
        icon.setTextColor(GOLD);
        icon.setTextSize(22);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(background(BG, 10));

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(dp(44), dp(44));
        card.addView(icon, iconParams);

        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setPadding(dp(12), 0, 0, 0);

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(14);
        title.setTypeface(null, Typeface.BOLD);
        textColumn.addView(title);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(11);
        info.setPadding(0, dp(4), 0, 0);
        textColumn.addView(info);

        card.addView(textColumn,
                new LinearLayout.LayoutParams(0, -2, 1));

        parent.addView(card);
    }

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setBackground(background(CARD, 14));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(11);
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
