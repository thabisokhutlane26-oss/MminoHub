
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(10, 15, 25);
    private final int CARD = Color.rgb(21, 30, 43);
    private final int GOLD = Color.rgb(217, 164, 65);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 180, 195);

    private LinearLayout root;
    private LinearLayout content;
    private TextView activeNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showHome();
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    private void showHome() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(20), dp(18), dp(20), dp(12));

        TextView logo = text("MminoHub", 27, GOLD, true);
        header.addView(logo, new LinearLayout.LayoutParams(0, -2, 1));

        TextView notification = text("♧", 27, WHITE, false);
        header.addView(notification);

        root.addView(header);

        TextView tagline = text(
            "BASOTHO MUSIC  •  BASOTHO PRIDE",
            11, MUTED, true
        );
        tagline.setPadding(dp(20), 0, dp(20), dp(18));
        root.addView(tagline);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(18), dp(8), dp(18), dp(24));

        scroll.addView(content);
        root.addView(scroll, new LinearLayout.LayoutParams(
            -1, 0, 1
        ));

        addWelcome();
        addSection("Explore MminoHub");
        addCategory("♫", "New Releases",
            "Discover the latest music from Lesotho");
        addCategory("▶", "Popular Music",
            "Find music listeners are enjoying");
        addCategory("♬", "Artists",
            "Meet Basotho musicians and groups");
        addCategory("▤", "Albums & EPs",
            "Explore complete music collections");

        addSection("Your MminoHub");
        addCategory("♡", "Your Library",
            "Your saved and purchased music");
        addCategory("◎", "Following",
            "Artists you choose to follow");

        addBottomNavigation();

        setContentView(root);
    }

    private void addWelcome() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(20), dp(22), dp(20), dp(22));
        card.setBackgroundColor(CARD);

        TextView title = text(
            "Discover the sound of Lesotho.",
            25, WHITE, true
        );

        TextView subtitle = text(
            "Explore Basotho artists, songs and albums in one place.",
            14, MUTED, false
        );
        subtitle.setPadding(0, dp(10), 0, 0);

        card.addView(title);
        card.addView(subtitle);

        content.addView(card, new LinearLayout.LayoutParams(
            -1, -2
        ));
    }

    private void addSection(String title) {
        TextView heading = text(title, 19, WHITE, true);
        heading.setPadding(0, dp(24), 0, dp(12));
        content.addView(heading);
    }

    private void addCategory(
        String symbol, String title, String description
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(dp(15), dp(16), dp(15), dp(16));
        card.setBackgroundColor(CARD);

        TextView icon = text(symbol, 26, GOLD, true);
        icon.setGravity(Gravity.CENTER);
        card.addView(icon, new LinearLayout.LayoutParams(
            dp(45), dp(45)
        ));

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);

        details.addView(text(title, 16, WHITE, true));

        TextView desc = text(description, 12, MUTED, false);
        desc.setPadding(0, dp(5), 0, 0);
        details.addView(desc);

        card.addView(details, new LinearLayout.LayoutParams(
            0, -2, 1
        ));

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(10);

        content.addView(card, params);
    }

    private void addBottomNavigation() {
        LinearLayout nav = new LinearLayout(this);
        nav.setBackgroundColor(CARD);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(0, dp(10), 0, dp(10));

        String[] labels = {
            "⌂\nHome",
            "⌕\nDiscover",
            "♫\nLibrary",
            "♡\nFollowing",
            "◎\nProfile"
        };

        for (int i = 0; i < labels.length; i++) {
            final String destination =
                labels[i].substring(labels[i].indexOf('\n') + 1);

            TextView item = text(
                labels[i], 11,
                i == 0 ? GOLD : MUTED,
                i == 0
            );
            item.setGravity(Gravity.CENTER);
            item.setOnClickListener(v ->
                openSection(destination)
            );

            nav.addView(item, new LinearLayout.LayoutParams(
                0, dp(48), 1
            ));
        }

        root.addView(nav);
    }

    private void openSection(String destination) {
        if (destination.equals("Home")) {
            showHome();
            return;
        }

        new android.app.AlertDialog.Builder(this)
            .setTitle(destination)
            .setMessage(
                destination + " will be built in the next development stage."
            )
            .setPositiveButton("OK", null)
            .show();
    }
}
