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

public class ListenerSupportActivity extends Activity {

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
        title.setText("Help & Support");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "We want your MminoHub experience to be smooth."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        LinearLayout welcome = createCard();

        TextView welcomeTitle = new TextView(this);
        welcomeTitle.setText("How can we help?");
        welcomeTitle.setTextColor(GOLD);
        welcomeTitle.setTextSize(18);
        welcomeTitle.setTypeface(null, Typeface.BOLD);
        welcome.addView(welcomeTitle);

        TextView welcomeText = new TextView(this);
        welcomeText.setText(
                "Find guidance about listening, purchases, " +
                "your account and supporting Basotho artists."
        );
        welcomeText.setTextColor(MUTED);
        welcomeText.setTextSize(12);
        welcomeText.setPadding(0, dp(8), 0, 0);
        welcome.addView(welcomeText);

        root.addView(welcome);

        addSection(root, "COMMON QUESTIONS");

        addQuestion(
                root,
                "How do I listen to music?",
                "Discover music from Basotho artists. " +
                "Available previews and full tracks will depend " +
                "on the release and your account access."
        );

        addQuestion(
                root,
                "How do I buy music?",
                "When purchases become available, you will be " +
                "able to select a release and complete payment " +
                "using supported payment methods."
        );

        addQuestion(
                root,
                "What happens when Premium ends?",
                "Individually purchased music is intended to " +
                "remain yours. Premium-only streaming and " +
                "offline access will follow subscription rules."
        );

        addQuestion(
                root,
                "How do artists earn money?",
                "MminoHub plans to share eligible music revenue " +
                "with artists. The proposed starting split is " +
                "80% for artists and 20% for MminoHub, subject " +
                "to a final published fee and royalty policy."
        );

        addQuestion(
                root,
                "How can I report a problem?",
                "Contact options will be provided when the " +
                "official MminoHub support service is ready."
        );

        addSection(root, "CONTACT SUPPORT");

        LinearLayout contact = createCard();

        TextView contactTitle = new TextView(this);
        contactTitle.setText("Need more assistance?");
        contactTitle.setTextColor(WHITE);
        contactTitle.setTextSize(15);
        contactTitle.setTypeface(null, Typeface.BOLD);
        contact.addView(contactTitle);

        TextView contactInfo = new TextView(this);
        contactInfo.setText(
                "Official support email and in-app support " +
                "messaging are not connected yet. We will add " +
                "verified contact details before launch."
        );
        contactInfo.setTextColor(MUTED);
        contactInfo.setTextSize(12);
        contactInfo.setPadding(0, dp(7), 0, dp(14));
        contact.addView(contactInfo);

        TextView button = new TextView(this);
        button.setText("SUPPORT COMING SOON");
        button.setTextColor(BG);
        button.setTextSize(12);
        button.setTypeface(null, Typeface.BOLD);
        button.setGravity(Gravity.CENTER);
        button.setPadding(dp(12), dp(13), dp(12), dp(13));
        button.setBackground(background(GOLD, 10));
        contact.addView(button);

        button.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "Official support will be available soon.",
                        Toast.LENGTH_LONG
                ).show()
        );

        root.addView(contact);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • HERE FOR BASOTHO MUSIC");
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

    private void addQuestion(
            LinearLayout parent,
            String question,
            String answer
    ) {
        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText(question);
        heading.setTextColor(WHITE);
        heading.setTextSize(14);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView detail = new TextView(this);
        detail.setText(answer);
        detail.setTextColor(MUTED);
        detail.setTextSize(12);
        detail.setLineSpacing(dp(3), 1.0f);
        detail.setPadding(0, dp(7), 0, 0);
        card.addView(detail);

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
