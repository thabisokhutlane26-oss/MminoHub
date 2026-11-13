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

public class ReleaseManagementActivity extends Activity {

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
        back.setPadding(0, 0, 0, dp(20));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        TextView title = new TextView(this);
        title.setText("Release Management");
        title.setTextColor(WHITE);
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Track your music releases and approval progress."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addSummaryCard(root);

        addSection(root, "RELEASE STATUS");

        addReleaseStatus(
                root,
                "DRAFT",
                "Not submitted",
                "Complete your release details before submission.",
                Color.rgb(155, 165, 180)
        );

        addReleaseStatus(
                root,
                "SUBMITTED",
                "Received by MminoHub",
                "Your release has been submitted for review.",
                Color.rgb(100, 181, 246)
        );

        addReleaseStatus(
                root,
                "PENDING REVIEW",
                "Waiting for administrator",
                "The review team has not completed its checks.",
                GOLD
        );

        addReleaseStatus(
                root,
                "APPROVED",
                "Ready for publication",
                "Your release has passed the approval process.",
                Color.rgb(76, 210, 145)
        );

        addReleaseStatus(
                root,
                "REJECTED",
                "Changes required",
                "Review the administrator's reason and correct the release.",
                Color.rgb(255, 110, 110)
        );

        addSection(root, "IMPORTANT");

        TextView information = new TextView(this);
        information.setText(
                "Release statuses shown here describe the MminoHub " +
                "workflow. Your actual release records and status " +
                "updates will appear after the secure backend and " +
                "administrator review system are connected."
        );
        information.setTextColor(MUTED);
        information.setTextSize(12);
        information.setPadding(dp(15), dp(15), dp(15), dp(15));
        information.setBackground(background(CARD, 14));
        root.addView(information);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • RELEASE CONTROL");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addSummaryCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));

        TextView heading = new TextView(this);
        heading.setText("MY RELEASES");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView count = new TextView(this);
        count.setText("0");
        count.setTextColor(WHITE);
        count.setTextSize(32);
        count.setTypeface(null, Typeface.BOLD);
        count.setPadding(0, dp(7), 0, dp(3));
        card.addView(count);

        TextView description = new TextView(this);
        description.setText(
                "No release records have been loaded yet."
        );
        description.setTextColor(MUTED);
        description.setTextSize(12);
        card.addView(description);

        parent.addView(card);
    }

    private void addSection(LinearLayout parent, String text) {
        TextView section = new TextView(this);
        section.setText(text);
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = dp(24);
        params.bottomMargin = dp(12);
        parent.addView(section, params);
    }

    private void addReleaseStatus(
            LinearLayout parent,
            String status,
            String heading,
            String description,
            int statusColor
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setBackground(background(CARD, 13));

        TextView badge = new TextView(this);
        badge.setText(status);
        badge.setTextColor(statusColor);
        badge.setTextSize(10);
        badge.setTypeface(null, Typeface.BOLD);
        card.addView(badge);

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, dp(7), 0, dp(5));
        card.addView(title);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        card.addView(info);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(10);
        parent.addView(card, params);
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
