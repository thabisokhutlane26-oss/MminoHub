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
import android.widget.Toast;

public class ListenerLibraryActivity extends Activity {

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
        title.setText("My Music Library");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Your music collection, all in one place."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addOverview(root);

        addSection(root, "MY COLLECTION");

        addLibraryCard(
                root,
                "♫",
                "Saved Songs",
                "Music you save for later.",
                "No saved songs yet."
        );

        addLibraryCard(
                root,
                "◆",
                "Purchased Music",
                "Singles, EPs and albums you own.",
                "Your purchased music will appear here."
        );

        addLibraryCard(
                root,
                "↓",
                "Downloaded Music",
                "Music available for offline listening.",
                "No downloaded music yet."
        );

        addSection(root, "PLAYLISTS");

        addPlaylistCard(root);

        addSection(root, "LIBRARY INFORMATION");

        TextView information = new TextView(this);
        information.setText(
                "Your library will be connected to your MminoHub " +
                "account. Purchased music is intended to remain " +
                "available after a Premium subscription ends. " +
                "Premium-only offline access will follow subscription " +
                "eligibility rules."
        );
        information.setTextColor(MUTED);
        information.setTextSize(12);
        information.setPadding(dp(15), dp(15), dp(15), dp(15));
        information.setBackground(background(CARD, 14));
        root.addView(information);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • YOUR MUSIC");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addOverview(LinearLayout parent) {
        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText("YOUR COLLECTION");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView count = new TextView(this);
        count.setText("0 songs");
        count.setTextColor(WHITE);
        count.setTextSize(25);
        count.setTypeface(null, Typeface.BOLD);
        count.setPadding(0, dp(8), 0, dp(4));
        card.addView(count);

        TextView description = new TextView(this);
        description.setText(
                "Your collection is waiting for your first Basotho tracks."
        );
        description.setTextColor(MUTED);
        description.setTextSize(12);
        card.addView(description);

        parent.addView(card);
    }

    private void addSection(LinearLayout parent, String label) {
        TextView section = new TextView(this);
        section.setText(label);
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.topMargin = dp(24);
        params.bottomMargin = dp(12);

        parent.addView(section, params);
    }

    private void addLibraryCard(
            LinearLayout parent,
            String symbol,
            String heading,
            String description,
            String emptyMessage
    ) {
        LinearLayout card = createCard();

        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);

        TextView icon = new TextView(this);
        icon.setText(symbol);
        icon.setTextColor(GOLD);
        icon.setTextSize(23);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(background(BG, 10));

        LinearLayout.LayoutParams iconParams =
                new LinearLayout.LayoutParams(dp(46), dp(46));

        row.addView(icon, iconParams);

        LinearLayout textColumn = new LinearLayout(this);
        textColumn.setOrientation(LinearLayout.VERTICAL);
        textColumn.setPadding(dp(12), 0, 0, 0);

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        textColumn.addView(title);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(11);
        info.setPadding(0, dp(4), 0, 0);
        textColumn.addView(info);

        row.addView(textColumn,
                new LinearLayout.LayoutParams(0, -2, 1));

        card.addView(row);

        TextView empty = new TextView(this);
        empty.setText(emptyMessage);
        empty.setTextColor(MUTED);
        empty.setTextSize(11);
        empty.setPadding(0, dp(14), 0, 0);
        card.addView(empty);

        parent.addView(card);
    }

    private void addPlaylistCard(LinearLayout parent) {
        LinearLayout card = createCard();

        TextView title = new TextView(this);
        title.setText("Your playlists");
        title.setTextColor(WHITE);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView info = new TextView(this);
        info.setText(
                "Create collections of your favourite Basotho music."
        );
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(6), 0, dp(14));
        card.addView(info);

        TextView empty = new TextView(this);
        empty.setText("No playlists created yet.");
        empty.setTextColor(MUTED);
        empty.setTextSize(11);
        card.addView(empty);

        TextView create = new TextView(this);
        create.setText("+  CREATE PLAYLIST");
        create.setTextColor(GOLD);
        create.setTextSize(12);
        create.setTypeface(null, Typeface.BOLD);
        create.setPadding(0, dp(18), 0, dp(3));

        create.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Playlist creation will be enabled " +
                        "when library storage is connected.",
                        Toast.LENGTH_LONG
                ).show()
        );

        card.addView(create);
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
