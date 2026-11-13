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

import java.util.Locale;

public class ArtistEarningsActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int GREEN = Color.rgb(76, 210, 145);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private static final double ARTIST_PERCENT = 80.0;
    private static final double PLATFORM_PERCENT = 20.0;

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
        title.setText("Earnings & Royalties");
        title.setTextColor(WHITE);
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Understand how your music revenue is shared."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addBalanceCard(root);

        addSection(root, "REVENUE SPLIT");
        addSplitCard(root);

        addSection(root, "ROYALTY CALCULATOR");
        addCalculatorCard(root);

        addSection(root, "TRANSACTION HISTORY");
        addEmptyHistory(root);

        addSection(root, "PAYOUT STATUS");
        addPayoutCard(root);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • ARTIST FINANCES");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addBalanceCard(LinearLayout parent) {
        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText("TOTAL ARTIST EARNINGS");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView amount = new TextView(this);
        amount.setText("M 0.00");
        amount.setTextColor(WHITE);
        amount.setTextSize(32);
        amount.setTypeface(null, Typeface.BOLD);
        amount.setPadding(0, dp(8), 0, dp(5));
        card.addView(amount);

        TextView note = new TextView(this);
        note.setText(
                "No verified sales have been loaded yet."
        );
        note.setTextColor(MUTED);
        note.setTextSize(12);
        card.addView(note);

        addBalanceRow(card, "Pending earnings", "M 0.00");
        addBalanceRow(card, "Available for payout", "M 0.00");

        parent.addView(card);
    }

    private void addBalanceRow(
            LinearLayout parent,
            String label,
            String amount
    ) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(0, dp(13), 0, 0);

        TextView left = new TextView(this);
        left.setText(label);
        left.setTextColor(MUTED);
        left.setTextSize(12);

        TextView right = new TextView(this);
        right.setText(amount);
        right.setTextColor(GREEN);
        right.setTextSize(13);
        right.setTypeface(null, Typeface.BOLD);

        row.addView(left, new LinearLayout.LayoutParams(
                0, -2, 1
        ));
        row.addView(right);

        parent.addView(row);
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

    private void addSplitCard(LinearLayout parent) {
        LinearLayout card = createCard();

        addSplitRow(
                card,
                "Artist share",
                formatPercent(ARTIST_PERCENT),
                GREEN
        );

        addSplitRow(
                card,
                "MminoHub share",
                formatPercent(PLATFORM_PERCENT),
                GOLD
        );

        TextView note = new TextView(this);
        note.setText(
                "Proposed revenue split: 80% for the artist and " +
                "20% for MminoHub. The final agreement must define " +
                "how payment fees, refunds and taxes are handled."
        );
        note.setTextColor(MUTED);
        note.setTextSize(12);
        note.setPadding(0, dp(15), 0, 0);
        card.addView(note);

        parent.addView(card);
    }

    private void addSplitRow(
            LinearLayout parent,
            String label,
            String percentage,
            int color
    ) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(0, dp(5), 0, dp(12));

        TextView name = new TextView(this);
        name.setText(label);
        name.setTextColor(WHITE);
        name.setTextSize(14);

        TextView value = new TextView(this);
        value.setText(percentage);
        value.setTextColor(color);
        value.setTextSize(17);
        value.setTypeface(null, Typeface.BOLD);

        row.addView(name, new LinearLayout.LayoutParams(
                0, -2, 1
        ));
        row.addView(value);

        parent.addView(row);
    }

    private void addCalculatorCard(LinearLayout parent) {
        LinearLayout card = createCard();

        addCalculatorExample(card, 10.00);
        addCalculatorExample(card, 50.00);
        addCalculatorExample(card, 100.00);

        TextView note = new TextView(this);
        note.setText(
                "Examples only. These calculations use the proposed " +
                "80/20 split before any fees, refunds or taxes."
        );
        note.setTextColor(MUTED);
        note.setTextSize(11);
        note.setPadding(0, dp(10), 0, 0);
        card.addView(note);

        parent.addView(card);
    }

    private void addCalculatorExample(
            LinearLayout parent,
            double revenue
    ) {
        double artist = revenue * ARTIST_PERCENT / 100.0;
        double platform = revenue * PLATFORM_PERCENT / 100.0;

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(0, dp(5), 0, dp(12));

        TextView total = new TextView(this);
        total.setText("Gross revenue: " + money(revenue));
        total.setTextColor(WHITE);
        total.setTextSize(14);
        total.setTypeface(null, Typeface.BOLD);
        row.addView(total);

        TextView breakdown = new TextView(this);
        breakdown.setText(
                "Artist: " + money(artist) +
                "     MminoHub: " + money(platform)
        );
        breakdown.setTextColor(MUTED);
        breakdown.setTextSize(12);
        breakdown.setPadding(0, dp(4), 0, 0);
        row.addView(breakdown);

        parent.addView(row);
    }

    private void addEmptyHistory(LinearLayout parent) {
        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText("No transactions yet");
        heading.setTextColor(WHITE);
        heading.setTextSize(15);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView info = new TextView(this);
        info.setText(
                "Verified purchases, streaming royalties, refunds " +
                "and adjustments will appear here once the payment " +
                "and royalty backend is connected."
        );
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(7), 0, 0);
        card.addView(info);

        parent.addView(card);
    }

    private void addPayoutCard(LinearLayout parent) {
        LinearLayout card = createCard();

        TextView status = new TextView(this);
        status.setText("NOT CONNECTED");
        status.setTextColor(GOLD);
        status.setTextSize(12);
        status.setTypeface(null, Typeface.BOLD);
        card.addView(status);

        TextView info = new TextView(this);
        info.setText(
                "Artist payouts through M-Pesa and EcoCash are " +
                "planned. Provider integration, account verification " +
                "and payout security must be completed before payments " +
                "can be requested or processed."
        );
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(8), 0, 0);
        card.addView(info);

        parent.addView(card);
    }

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));
        return card;
    }

    private String formatPercent(double value) {
        return String.format(
                Locale.US, "%.0f%%", value
        );
    }

    private String money(double value) {
        return String.format(
                Locale.US, "M %.2f", value
        );
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
