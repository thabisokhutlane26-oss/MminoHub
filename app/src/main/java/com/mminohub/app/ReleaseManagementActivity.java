
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

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
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
                "Manage your Singles, EPs and Albums."
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
                Release.STATUS_DRAFT,
                "Your release is still being prepared.",
                Color.rgb(155, 165, 180)
        );

        addStatusGuide(
                root,
                Release.STATUS_SUBMITTED,
                "Your release has been sent for review.",
                Color.rgb(100, 181, 246)
        );

        addStatusGuide(
                root,
                Release.STATUS_APPROVED,
                "Your release has passed the approval process.",
                Color.rgb(76, 210, 145)
        );

        addStatusGuide(
                root,
                Release.STATUS_REJECTED,
                "Review the rejection reason before making changes.",
                Color.rgb(255, 110, 110)
        );

        addStatusGuide(
                root,
                Release.STATUS_PUBLISHED,
                "Your release has been published.",
                GOLD
        );

        addSection(root, "IMPORTANT");

        TextView information = new TextView(this);
        information.setText(
                "Release records are displayed from your artist " +
                "account. Approval and publication must be handled " +
                "through the authorised MminoHub review process."
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
        heading.setText("TOTAL RELEASES");
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
        summaryDescription.setText("Loading your releases...");
        summaryDescription.setTextColor(MUTED);
        summaryDescription.setTextSize(12);
        card.addView(summaryDescription);

        parent.addView(card);
    }

    private void loadReleases() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            releaseCount.setText("0");
            summaryDescription.setText("No signed-in artist account.");

            showMessage(
                    "SIGN IN REQUIRED",
                    "Sign in to view your release records."
            );
            return;
        }

        releaseCount.setText("...");
        summaryDescription.setText("Loading your releases...");

        firestore.collection("artistReleases")
                .whereEqualTo("artistId", user.getUid())
                .get()
                .addOnSuccessListener(this::displayReleases)
                .addOnFailureListener(error -> {
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    releaseCount.setText("--");
                    summaryDescription.setText(
                            "Your release records could not be loaded."
                    );

                    showMessage(
                            "RELEASES UNAVAILABLE",
                            "Check your internet connection and Firestore " +
                            "permissions. Please try again later."
                    );

                    android.util.Log.e(
                            "MminoHub",
                            "Release loading failed",
                            error
                    );
                });
    }

    private void displayReleases(QuerySnapshot snapshot) {
        if (isFinishing() || isDestroyed()) {
            return;
        }

        releaseContainer.removeAllViews();

        int count = snapshot.size();
        releaseCount.setText(String.valueOf(count));

        if (count == 0) {
            summaryDescription.setText(
                    "No releases have been saved yet."
            );

            showMessage(
                    "NO RELEASES YET",
                    "Your Singles, EPs and Albums will appear here " +
                    "when release records are saved to MminoHub."
            );
            return;
        }

        summaryDescription.setText(
                count == 1
                        ? "1 release found in your account."
                        : count + " releases found in your account."
        );

        for (DocumentSnapshot document : snapshot.getDocuments()) {
            String id = document.getId();

            String title = document.getString("title");
            String description = document.getString("description");
            String type = document.getString("type");
            String status = document.getString("status");
            String rejectionReason =
                    document.getString("rejectionReason");

            if (rejectionReason == null) {
                rejectionReason = document.getString("reviewNote");
            }

            if (title == null || title.trim().isEmpty()) {
                title = "Untitled release";
            }

            if (description == null) {
                description = "";
            }

            if (type == null) {
                type = "RELEASE";
            }

            if (status == null || status.trim().isEmpty()) {
                status = Release.STATUS_DRAFT;
            }

            long releaseDate = getReleaseDate(document);
            long trackCount = getTrackCount(document);

            addReleaseCard(
                    releaseContainer,
                    id,
                    title,
                    description,
                    type,
                    status,
                    releaseDate,
                    trackCount,
                    rejectionReason
            );
        }
    }

    private void addReleaseCard(
            LinearLayout parent,
            String id,
            String releaseTitle,
            String description,
            String type,
            String status,
            long releaseDate,
            long trackCount,
            String rejectionReason
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
        title.setTextSize(17);
        title.setTypeface(null, Typeface.BOLD);
        title.setPadding(0, dp(9), 0, dp(5));
        card.addView(title);

        TextView typeView = new TextView(this);
        typeView.setText(formatType(type));
        typeView.setTextColor(GOLD);
        typeView.setTextSize(11);
        typeView.setTypeface(null, Typeface.BOLD);
        card.addView(typeView);

        if (!description.trim().isEmpty()) {
            TextView descriptionView = new TextView(this);
            descriptionView.setText(description);
            descriptionView.setTextColor(MUTED);
            descriptionView.setTextSize(12);
            descriptionView.setPadding(0, dp(8), 0, 0);
            card.addView(descriptionView);
        }

        TextView tracks = new TextView(this);
        tracks.setText("Tracks: " + trackCount);
        tracks.setTextColor(MUTED);
        tracks.setTextSize(12);
        tracks.setPadding(0, dp(8), 0, 0);
        card.addView(tracks);

        if (releaseDate > 0) {
            TextView date = new TextView(this);
            date.setText("Release date: " + formatDate(releaseDate));
            date.setTextColor(MUTED);
            date.setTextSize(12);
            date.setPadding(0, dp(5), 0, 0);
            card.addView(date);
        }

        String statusDescription = getStatusDescription(status);

        TextView statusInfo = new TextView(this);
        statusInfo.setText(statusDescription);
        statusInfo.setTextColor(MUTED);
        statusInfo.setTextSize(12);
        statusInfo.setPadding(0, dp(10), 0, 0);
        card.addView(statusInfo);

        if (Release.STATUS_REJECTED.equals(normalizeStatus(status))
                && rejectionReason != null
                && !rejectionReason.trim().isEmpty()) {

            TextView rejection = new TextView(this);
            rejection.setText(
                    "REVIEW REASON\n" + rejectionReason.trim()
            );
            rejection.setTextColor(
                    Color.rgb(255, 140, 140)
            );
            rejection.setTextSize(12);
            rejection.setPadding(0, dp(10), 0, 0);
            card.addView(rejection);
        }

        TextView reference = new TextView(this);
        reference.setText("Release ID: " + id);
        reference.setTextColor(
                Color.rgb(120, 135, 150)
        );
        reference.setTextSize(9);
        reference.setPadding(0, dp(12), 0, 0);
        card.addView(reference);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(12);
        parent.addView(card, params);
    }

    private long getReleaseDate(DocumentSnapshot document) {
        Object value = document.get("releaseDate");

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        if (value instanceof Timestamp) {
            return ((Timestamp) value).toDate().getTime();
        }

        Object createdAt = document.get("createdAt");

        if (createdAt instanceof Timestamp) {
            return ((Timestamp) createdAt).toDate().getTime();
        }

        if (createdAt instanceof Number) {
            return ((Number) createdAt).longValue();
        }

        return 0;
    }

    private long getTrackCount(DocumentSnapshot document) {
        Object value = document.get("trackCount");

        if (value instanceof Number) {
            return ((Number) value).longValue();
        }

        return 0;
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat(
                "dd MMM yyyy",
                Locale.getDefault()
        );

        return format.format(new Date(timestamp));
    }

    private String normalizeStatus(String status) {
        if (status == null) {
            return Release.STATUS_DRAFT;
        }

        String value = status.trim()
                .toUpperCase(Locale.ROOT)
                .replace(' ', '_');

        if (value.equals("PENDING")
                || value.equals("PENDING_REVIEW")) {
            return Release.STATUS_SUBMITTED;
        }

        return value;
    }

    private String formatStatus(String status) {
        return normalizeStatus(status).replace('_', ' ');
    }

    private String formatType(String type) {
        if (type == null) {
            return "RELEASE";
        }

        String value = type.trim().toUpperCase(Locale.ROOT);

        if (value.equals(Release.TYPE_SINGLE)) {
            return "SINGLE";
        }

        if (value.equals(Release.TYPE_EP)) {
            return "EXTENDED PLAY (EP)";
        }

        if (value.equals(Release.TYPE_ALBUM)) {
            return "ALBUM";
        }

        return value.replace('_', ' ');
    }

    private int getStatusColor(String status) {
        String value = normalizeStatus(status);

        if (value.equals(Release.STATUS_APPROVED)
                || value.equals(Release.STATUS_PUBLISHED)) {
            return Color.rgb(76, 210, 145);
        }

        if (value.equals(Release.STATUS_REJECTED)) {
            return Color.rgb(255, 110, 110);
        }

        if (value.equals(Release.STATUS_SUBMITTED)) {
            return Color.rgb(100, 181, 246);
        }

        return Color.rgb(155, 165, 180);
    }

    private String getStatusDescription(String status) {
        String value = normalizeStatus(status);

        if (value.equals(Release.STATUS_DRAFT)) {
            return "This release is still being prepared.";
        }

        if (value.equals(Release.STATUS_SUBMITTED)) {
            return "Your release has been submitted and is awaiting review.";
        }

        if (value.equals(Release.STATUS_APPROVED)) {
            return "Your release has passed the approval process.";
        }

        if (value.equals(Release.STATUS_REJECTED)) {
            return "Your release was not approved. Check the review reason.";
        }

        if (value.equals(Release.STATUS_PUBLISHED)) {
            return "Your release has been published.";
        }

        return "Release status: " + formatStatus(status);
    }

    private void showMessage(String heading, String description) {
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

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(10);
        releaseContainer.addView(card, params);
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
        badge.setText(status.replace('_', ' '));
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
        drawable.setStroke(
                dp(1),
                Color.rgb(49, 62, 77)
        );
        return drawable;
    }
}
