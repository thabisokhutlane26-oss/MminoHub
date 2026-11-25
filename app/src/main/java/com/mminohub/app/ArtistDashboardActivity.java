
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class ArtistDashboardActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private TextView artistName;
    private TextView applicationStatus;
    private TextView applicationDescription;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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
        title.setText("Artist Dashboard");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        artistName = new TextView(this);
        artistName.setText("Welcome, Artist");
        artistName.setTextColor(GOLD);
        artistName.setTextSize(16);
        artistName.setTypeface(null, Typeface.BOLD);
        artistName.setPadding(0, dp(7), 0, 0);
        root.addView(artistName);

        TextView subtitle = new TextView(this);
        subtitle.setText("Your music. Your audience. Your growth.");
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(5), 0, dp(22));
        root.addView(subtitle);

        addApplicationCard(root);

        addEarningsCard(root);

        addSection(root, "PERFORMANCE");

        LinearLayout firstRow = new LinearLayout(this);
        firstRow.setOrientation(LinearLayout.HORIZONTAL);

        addMetric(firstRow, "STREAMS", "0");
        addMetric(firstRow, "PURCHASES", "0");

        root.addView(firstRow);

        LinearLayout secondRow = new LinearLayout(this);
        secondRow.setOrientation(LinearLayout.HORIZONTAL);

        addMetric(secondRow, "FOLLOWERS", "0");
        addMetric(secondRow, "RELEASES", "0");

        root.addView(secondRow);

        addSection(root, "ARTIST ACTIVITY");

        addActivityCard(
                root,
                "Music releases",
                "Your submitted and approved releases will appear here."
        );

        addActivityCard(
                root,
                "Audience growth",
                "Your listener and follower statistics will appear here."
        );

        addActivityCard(
                root,
                "Earnings statements",
                "Your verified sales, royalties and payout records will appear here."
        );

        addSection(root, "PAYOUT INFORMATION");

        TextView payout = new TextView(this);
        payout.setText(
                "MminoHub plans to support M-Pesa and EcoCash " +
                "artist payouts, subject to payment-provider approval. " +
                "No payout balance or payment is available yet."
        );
        payout.setTextColor(MUTED);
        payout.setTextSize(12);
        payout.setPadding(dp(15), dp(15), dp(15), dp(15));
        payout.setBackground(background(CARD, 14));
        root.addView(payout);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • ARTIST CENTRE");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);

        loadArtistApplication();
    }

    private void addApplicationCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));

        TextView heading = new TextView(this);
        heading.setText("ARTIST ACCOUNT STATUS");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        applicationStatus = new TextView(this);
        applicationStatus.setText("CHECKING STATUS...");
        applicationStatus.setTextColor(WHITE);
        applicationStatus.setTextSize(19);
        applicationStatus.setTypeface(null, Typeface.BOLD);
        applicationStatus.setPadding(0, dp(9), 0, dp(5));
        card.addView(applicationStatus);

        applicationDescription = new TextView(this);
        applicationDescription.setText(
                "Loading your artist application."
        );
        applicationDescription.setTextColor(MUTED);
        applicationDescription.setTextSize(12);
        card.addView(applicationDescription);

        parent.addView(card);
    }

    private void loadArtistApplication() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            artistName.setText("Welcome to MminoHub");
            applicationStatus.setText("SIGN IN REQUIRED");
            applicationDescription.setText(
                    "Sign in to view your artist dashboard."
            );
            return;
        }

        artistName.setText("Welcome, " + getDisplayName(user));

        firestore.collection("artistApplications")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(this::displayArtistApplication)
                .addOnFailureListener(error -> {
                    if (isFinishing()) {
                        return;
                    }

                    applicationStatus.setText("STATUS UNAVAILABLE");
                    applicationDescription.setText(
                            "We could not load your artist application. " +
                            "Check your internet connection and try again."
                    );
                });
    }

    private String getDisplayName(FirebaseUser user) {
        String name = user.getDisplayName();

        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }

        String email = user.getEmail();

        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf("@"));
        }

        return "Artist";
    }

    private void displayArtistApplication(DocumentSnapshot document) {
        if (isFinishing()) {
            return;
        }

        if (!document.exists()) {
            applicationStatus.setText("NO ARTIST APPLICATION");
            applicationDescription.setText(
                    "Submit your artist registration to start your " +
                    "MminoHub artist journey."
            );
            return;
        }

        String name = document.getString("artistName");

        if (name != null && !name.trim().isEmpty()) {
            artistName.setText("Welcome, " + name.trim());
        }

        String status = document.getString("status");

        if (status == null) {
            status = "pending";
        }

        String normalStatus = status.toLowerCase(Locale.ROOT);

        if ("approved".equals(normalStatus)) {
            applicationStatus.setText("ARTIST APPROVED");
            applicationDescription.setText(
                    "Your artist application has been approved. " +
                    "You can continue preparing your music releases."
            );
        } else if ("rejected".equals(normalStatus)) {
            applicationStatus.setText("APPLICATION NOT APPROVED");
            applicationDescription.setText(
                    "Your artist application was not approved. " +
                    "Please contact MminoHub support for assistance."
            );
        } else {
            applicationStatus.setText("APPLICATION PENDING");
            applicationDescription.setText(
                    "Your artist application has been received and is " +
                    "waiting for review. Your account is not yet approved."
            );
        }
    }

    private void addEarningsCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(18), dp(18), dp(18));
        card.setBackground(background(CARD, 16));

        TextView label = new TextView(this);
        label.setText("TOTAL ARTIST EARNINGS");
        label.setTextColor(GOLD);
        label.setTextSize(11);
        label.setTypeface(null, Typeface.BOLD);
        card.addView(label);

        TextView amount = new TextView(this);
        amount.setText("M 0.00");
        amount.setTextColor(WHITE);
        amount.setTextSize(32);
        amount.setTypeface(null, Typeface.BOLD);
        amount.setPadding(0, dp(9), 0, dp(5));
        card.addView(amount);

        TextView note = new TextView(this);
        note.setText(
                "No verified earnings are available yet. Earnings " +
                "will be calculated from completed transactions."
        );
        note.setTextColor(MUTED);
        note.setTextSize(12);
        card.addView(note);

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

    private void addMetric(
            LinearLayout parent,
            String label,
            String value
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(13), dp(15), dp(10), dp(15));
        card.setBackground(background(CARD, 13));

        TextView number = new TextView(this);
        number.setText(value);
        number.setTextColor(WHITE);
        number.setTextSize(25);
        number.setTypeface(null, Typeface.BOLD);
        card.addView(number);

        TextView heading = new TextView(this);
        heading.setText(label);
        heading.setTextColor(GOLD);
        heading.setTextSize(10);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setPadding(0, dp(5), 0, 0);
        card.addView(heading);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(0, -2, 1);

        params.setMargins(0, 0, dp(8), dp(9));

        parent.addView(card, params);
    }

    private void addActivityCard(
            LinearLayout parent,
            String heading,
            String description
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setBackground(background(CARD, 13));

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(15);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(6), 0, 0);
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
