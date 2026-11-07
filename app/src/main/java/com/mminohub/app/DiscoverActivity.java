package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DiscoverActivity extends Activity {

    private final int BG = Color.rgb(10, 15, 25);
    private final int CARD = Color.rgb(21, 30, 43);
    private final int GOLD = Color.rgb(217, 164, 65);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 180, 195);

    private LinearLayout content;
    private String selectedCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDiscover();
    }

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density);
    }

    private TextView text(
            String value,
            int size,
            int color,
            boolean bold
    ) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(null, Typeface.BOLD);
        }

        return t;
    }

    private void showDiscover() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        // HEADER

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(
                dp(20), dp(18), dp(20), dp(15)
        );

        TextView title = text(
                "Discover", 27, GOLD, true
        );

        header.addView(title);
        root.addView(header);

        // SEARCH

        EditText search = new EditText(this);
        search.setSingleLine(true);
        search.setTextColor(WHITE);
        search.setHintTextColor(MUTED);
        search.setHint("Search songs, artists or albums");
        search.setTextSize(14);
        search.setPadding(
                dp(15), dp(12), dp(15), dp(12)
        );
        search.setBackgroundColor(CARD);

        LinearLayout.LayoutParams searchParams =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        searchParams.setMargins(
                dp(18), 0, dp(18), dp(18)
        );

        root.addView(search, searchParams);

        // SCROLL CONTENT

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(18), 0, dp(18), dp(25)
        );

        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addHeading("Explore music");

        addCategory(
                "All",
                "All music"
        );

        addCategory(
                "New Releases",
                "Recently released music"
        );

        addCategory(
                "Popular",
                "Popular songs"
        );

        addCategory(
                "Albums",
                "Albums and EPs"
        );

        addCategory(
                "Artists",
                "Basotho artists"
        );

        addEmptyState();

        // BOTTOM NAVIGATION

        addBottomNavigation(root);

        setContentView(root);
    }

    private void addHeading(String value) {

        TextView heading = text(
                value, 19, WHITE, true
        );

        heading.setPadding(
                0, dp(5), 0, dp(15)
        );

        content.addView(heading);
    }

    private void addCategory(
            String category,
            String description
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(
                dp(15), dp(15), dp(15), dp(15)
        );
        card.setBackgroundColor(CARD);
        card.setClickable(true);
        card.setFocusable(true);

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);

        details.addView(text(
                category, 16, WHITE, true
        ));

        TextView subtitle = text(
                description, 12, MUTED, false
        );

        subtitle.setPadding(0, dp(5), 0, 0);

        details.addView(subtitle);

        card.addView(details, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        TextView arrow = text(
                "›", 27, GOLD, true
        );

        card.addView(arrow);

        card.setOnClickListener(v -> {
            selectedCategory = category;
            updateEmptyState();
        });

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        params.bottomMargin = dp(9);

        content.addView(card, params);
    }

    private void addEmptyState() {

        LinearLayout empty = new LinearLayout(this);
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(
                dp(20), dp(30), dp(20), dp(30)
        );
        empty.setBackgroundColor(CARD);

        TextView symbol = text(
                "♫", 38, GOLD, true
        );

        symbol.setGravity(Gravity.CENTER);

        TextView message = text(
                "No music available yet.",
                17, WHITE, true
        );

        message.setGravity(Gravity.CENTER);
        message.setPadding(0, dp(12), 0, dp(7));

        TextView explanation = text(
                "Check back soon as Basotho artists join MminoHub.",
                13, MUTED, false
        );

        explanation.setGravity(Gravity.CENTER);

        empty.addView(symbol);
        empty.addView(message);
        empty.addView(explanation);

        empty.setTag("empty_state");

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        params.topMargin = dp(15);

        content.addView(empty, params);
    }

    private void updateEmptyState() {

        String message;

        if (selectedCategory.equals("Artists")) {
            message =
                    "No artists available yet. Basotho artists will appear here when they join MminoHub.";
        } else {
            message =
                    "No music available yet. Check back soon as Basotho artists join MminoHub.";
        }

        for (int i = 0; i < content.getChildCount(); i++) {

            View child = content.getChildAt(i);

            if ("empty_state".equals(child.getTag())
                    && child instanceof LinearLayout) {

                LinearLayout empty = (LinearLayout) child;

                TextView explanation =
                        (TextView) empty.getChildAt(2);

                explanation.setText(message);
            }
        }

        Toast.makeText(
                this,
                selectedCategory,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void addBottomNavigation(
            LinearLayout root
    ) {

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setBackgroundColor(CARD);
        nav.setPadding(0, dp(9), 0, dp(9));

        String[] labels = {
                "⌂\nHome",
                "⌕\nDiscover",
                "♫\nLibrary",
                "♡\nFollowing",
                "◎\nProfile"
        };

        for (int i = 0; i < labels.length; i++) {

            final String destination =
                    labels[i].substring(
                            labels[i].indexOf('\n') + 1
                    );

            TextView item = text(
                    labels[i],
                    11,
                    destination.equals("Discover")
                            ? GOLD : MUTED,
                    destination.equals("Discover")
            );

            item.setGravity(Gravity.CENTER);
            item.setClickable(true);

            item.setOnClickListener(v -> {

                if (destination.equals("Discover")) {
                    return;
                }

                if (destination.equals("Home")) {
                    finish();
                    return;
                }

                Toast.makeText(
                        this,
                        destination + " will be connected next.",
                        Toast.LENGTH_SHORT
                ).show();
            });

            nav.addView(item, new LinearLayout.LayoutParams(
                    0, dp(48), 1
            ));
        }

        root.addView(nav);
    }
}