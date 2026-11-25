
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;

public class ReleaseManagementActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private TextView releaseCount;
    private TextView summaryDescription;
    private LinearLayout releaseContainer;

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

        addSection(root, "MY RELEASES");

        releaseContainer = new LinearLayout(this);
        releaseContainer.setOrientation(LinearLayout.VERTICAL);
        root.addView(releaseContainer);

        addSection(root, "RELEASE STATUS GUIDE");

        addStatusGuide(
                root,
                "DRAFT",
                "Release details are being prepared.",
                Color.rgb(155, 165, 180)
        );

        addStatusGuide(
                root,
                "SUBMITTED",
                "The release has been sent for review.",
                Color.rgb(100, 181, 246)
        );

        addStatusGuide(
                root,
                "PENDING REVIEW",
                "The release is waiting for administrator review.",
                GOLD
        );

        addStatusGuide(
                root,
                "APPROVED",
                "The release has passed the approval process.",
                Color.rgb(76, 210, 145)
        );

        addStatusGuide(
                root,
                "REJECTED",
                "Changes may be required before resubmission.",
                Color.rgb(255, 110, 110)
        );

        addSection(root, "IMPORTANT");

        TextView information = new TextView(this);
        information.setText(
                "Only release records saved in MminoHub will appear " +
                "above. Approval and publication must be handled " +
                "through the authorised review process."
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

        loadReleases();
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

        releaseCount = new TextView(this);
        releaseCount.setText("...");
        releaseCount.setTextColor(WHITE);
        releaseCount.setTextSize(32);
        releaseCount.setTypeface(null, Typeface.BOLD);
        releaseCount.setPadding(0, dp(7), 0, dp(3));
        card.addView(releaseCount);

        summaryDescription = new TextView(this);
        summaryDescription.setText("Loading your release records...");
        summaryDescription.setTextColor(MUTED);
        summaryDescription.setTextSize(12);
        card.addView(summaryDescription);

        parent.addView(card);
    }

    private void loadReleases() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            showLoadMessage(
                    "SIGN IN REQUIRED",
                    "Sign in to view your release records."
            );
            releaseCount.setText("0");
            summaryDescription.setText("No signed-in artist account.");
            return;
        }

        firestore.collection("artistReleases")
                .whereEqualTo("artistId", user.getUid())
                .get()
                .addOnSuccessListener(this::displayReleases)
                .addOnFailureListener(error -> {
                    if (isFinishing()) {
                        return;
                    }

                    releaseCount.setText("--");
                    summaryDescription.setText(
                            "Release records could not be loaded."
                    );

                    showLoadMessage(
                            "RELEASES UNAVAILABLE",
                            "Check your connection and Firestore permissions. " +
                            "Release records will appear here when available."
                    );
                });
    }

    private void displayReleases(QuerySnapshot snapshot) {
        if (isFinishing()) {
            return;
        }

        releaseContainer.removeAllViews();

        int count = snapshot.size();
        releaseCount.setText(String.valueOf(count));

        if (count == 0) {
            summaryDescription.setText(
                    "No release records have been submitted yet."
            );

            showLoadMessage(
                    "NO RELEASES YET",
                    "Your saved music releases will appear here after " +
                    "they are submitted through the artist upload process."
            );
            return;
        }

        summaryDescription.setText(
                count == 1
                        ? "1 release record found."
                        : count + " release records found."
        );

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            String title = document.getString("title");
            String status = document.getString("status");
            String reviewNote = document.getString("reviewNote");

            if (title == null || title.trim().isEmpty()) {
                title = "Untitled release";
            }

            if (status == null || status.trim().isEmpty()) {
                status = "submitted";
            }

            addReleaseCard(
                    releaseContainer,
                    title,
                    status,
                    reviewNote
            );
        }
    }

    private void addReleaseCard(
            LinearLayout parent,
            String releaseTitle,
            String status,
            String reviewNote
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setBackground(background(CARD, 13));

        TextView badge = new TextView(this);
        badge.setText(formatStatus(status));
        badge.setTextColor(getStatusColor(status));
        badge.setTextSize(10);
        badge.setTypeface(null, Typeface.BOLD);
        card.addView(badge);

        TextView title = new TextView(this);
        title.setText(releaseTitle);
        title.setTextColor(WHITE);
        title.setTextSize(16);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, dp(8), 0, dp(5));
        card.addView(title);

        String description = getStatusDescription(status);

        if (reviewNote != null && !reviewNote.trim().isEmpty()) {
            description += "\n\nReview note: " + reviewNote.trim();
        }

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

    private String formatStatus(String status) {
        return status.replace('_', ' ')
                .toUpperCase(Locale.ROOT);
    }

    private int getStatusColor(String status) {
        String value = status.toLowerCase(Locale.ROOT);

        if (value.equals("approved") || value.equals("published")) {
            return Color.rgb(76, 210, 145);
        }

        if (value.equals("rejected")) {
            return Color.rgb(255, 110, 110);
        }

        if (value.equals("submitted")) {
            return Color.rgb(100, 181, 246);
        }

        if (value.equals("pending") || value.equals("pending_review")) {
            return GOLD;
        }

        return Color.rgb(155, 165, 180);
    }

    private String getStatusDescription(String status) {
        String value = status.toLowerCase(Locale.ROOT);

        if (value.equals("approved")) {
            return "Your release has been approved.";
        }

        if (value.equals("published")) {
            return "Your release has been published.";
        }

        if (value.equals("rejected")) {
            return "Your release was not approved. Check the review note.";
        }

        if (value.equals("pending") || value.equals("pending_review")) {
            return "Your release is waiting for administrator review.";
        }

        if (value.equals("submitted")) {
            return "Your release has been submitted to MminoHub.";
        }

        if (value.equals("draft")) {
            return "This release is still being prepared.";
        }

        return "Release status: " + formatStatus(status);
    }

    private void showLoadMessage(String heading, String description) {
        if (releaseContainer == null) {
            return;
        }

        releaseContainer.removeAllViews();

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(15), dp(15), dp(15));
        card.setBackground(background(CARD, 13));

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(GOLD);
        title.setTextSize(14);
        title.setTypeface(null, Typeface.BOLD);
        card.addView(title);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(7), 0, 0);
        card.addView(info);

        parentAdd(releaseContainer, card);
    }

    private void parentAdd(LinearLayout parent, LinearLayout card) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(10);
        parent.addView(card, params);
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

    private void addStatusGuide(
            LinearLayout parent,
            String status,
            String description,
            int statusColor
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(13), dp(15), dp(13));
        card.setBackground(background(CARD, 13));

        TextView badge = new TextView(this);
        badge.setText(status);
        badge.setTextColor(statusColor);
        badge.setTextSize(10);
        badge.setTypeface(null, Typeface.BOLD);
        card.addView(badge);

        TextView info = new TextView(this);
        info.setText(description);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(5), 0, 0);
        card.addView(info);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(9);
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
