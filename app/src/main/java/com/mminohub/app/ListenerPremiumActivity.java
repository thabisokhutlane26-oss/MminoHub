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

public class ListenerPremiumActivity extends Activity {

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
        title.setText("MminoHub Premium");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Experience more of Basotho music."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        LinearLayout hero = createCard();

        TextView crown = new TextView(this);
        crown.setText("★");
        crown.setTextColor(GOLD);
        crown.setTextSize(42);
        crown.setGravity(Gravity.CENTER);
        hero.addView(crown);

        TextView premium = new TextView(this);
        premium.setText("PREMIUM MEMBERSHIP");
        premium.setTextColor(GOLD);
        premium.setTextSize(12);
        premium.setTypeface(null, Typeface.BOLD);
        premium.setGravity(Gravity.CENTER);
        premium.setPadding(0, dp(8), 0, dp(12));
        hero.addView(premium);

        TextView price = new TextView(this);
        price.setText("M50");
        price.setTextColor(WHITE);
        price.setTextSize(38);
        price.setTypeface(null, Typeface.BOLD);
        price.setGravity(Gravity.CENTER);
        hero.addView(price);

        TextView period = new TextView(this);
        period.setText("per month • proposed price");
        period.setTextColor(MUTED);
        period.setTextSize(12);
        period.setGravity(Gravity.CENTER);
        period.setPadding(0, dp(4), 0, dp(18));
        hero.addView(period);

        TextView status = new TextView(this);
        status.setText("CURRENT STATUS: NOT SUBSCRIBED");
        status.setTextColor(GOLD);
        status.setTextSize(11);
        status.setTypeface(null, Typeface.BOLD);
        status.setGravity(Gravity.CENTER);
        status.setPadding(dp(10), dp(11), dp(10), dp(11));
        status.setBackground(background(BG, 10));
        hero.addView(status);

        root.addView(hero);

        addSection(root, "PREMIUM BENEFITS");

        addBenefit(root, "♫", "Full music catalogue",
                "Stream the full catalogue available to Premium members.");

        addBenefit(root, "↓", "Offline listening",
                "Eligible Premium music can be saved for offline access " +
                "while your subscription remains active.");

        addBenefit(root, "◆", "Support Basotho artists",
                "Your listening helps support the artists who create " +
                "music in Lesotho.");

        addSection(root, "IMPORTANT INFORMATION");

        LinearLayout infoCard = createCard();

        TextView info = new TextView(this);
        info.setText(
                "Music you purchase individually is intended to remain " +
                "in your library after Premium cancellation. Premium-only " +
                "streaming and offline access will follow subscription " +
                "eligibility rules.\n\n" +
                "The price and benefits shown are proposed. Payments, " +
                "subscriptions, renewals and cancellations are not " +
                "connected yet."
        );
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setLineSpacing(dp(4), 1.0f);
        infoCard.addView(info);

        root.addView(infoCard);

        TextView subscribe = new TextView(this);
        subscribe.setText("SUBSCRIPTION COMING SOON");
        subscribe.setTextColor(BG);
        subscribe.setTextSize(13);
        subscribe.setTypeface(null, Typeface.BOLD);
        subscribe.setGravity(Gravity.CENTER);
        subscribe.setPadding(dp(15), dp(16), dp(15), dp(16));
        subscribe.setBackground(background(GOLD, 12));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, -2);
        buttonParams.topMargin = dp(12);
        root.addView(subscribe, buttonParams);

        subscribe.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Premium payments are not available yet.",
                        Toast.LENGTH_LONG
                ).show()
        );

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • BASOTHO MUSIC, BASOTHO PRIDE");
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
        params.topMargin = dp(24);
        params.bottomMargin = dp(12);

        parent.addView(section, params);
    }

    private void addBenefit(
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
        icon.setTextSize(23);
        icon.setGravity(Gravity.CENTER);
        icon.setBackground(background(BG, 10));

        card.addView(icon,
                new LinearLayout.LayoutParams(dp(45), dp(45)));

        LinearLayout column = new LinearLayout(this);
        column.setOrientation(LinearLayout.VERTICAL);
        column.setPadding(dp(12), 0, 0, 0);

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(14);
        title.setTypeface(null, Typeface.BOLD);
        column.addView(title);

        TextView detail = new TextView(this);
        detail.setText(description);
        detail.setTextColor(MUTED);
        detail.setTextSize(11);
        detail.setPadding(0, dp(4), 0, 0);
        column.addView(detail);

        card.addView(column,
                new LinearLayout.LayoutParams(0, -2, 1));

        parent.addView(card);
    }

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
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
