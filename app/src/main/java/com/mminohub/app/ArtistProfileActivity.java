
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
import com.google.firebase.firestore.FirebaseFirestore;

public class ArtistProfileActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private TextView nameView;
    private TextView statusView;
    private TextView locationView;
    private TextView bioView;
    private TextView genreView;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private String artistName = "Artist name";
    private String district = "Lesotho";
    private String genre = "Basotho Music";
    private String biography =
            "This artist has not added a biography yet.";
    private String applicationStatus = "Not submitted";

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        artistName = getIntent().getStringExtra("artist_name");
        district = getIntent().getStringExtra("artist_district");
        genre = getIntent().getStringExtra("artist_genre");
        biography = getIntent().getStringExtra("artist_biography");

        if (artistName == null || artistName.trim().isEmpty()) {
            artistName = "Artist name";
        }

        if (district == null || district.trim().isEmpty()) {
            district = "Lesotho";
        }

        if (genre == null || genre.trim().isEmpty()) {
            genre = "Basotho Music";
        }

        if (biography == null || biography.trim().isEmpty()) {
            biography = "This artist has not added a biography yet.";
        }

        buildScreen();
        loadArtistApplication();
    }

    private void buildScreen() {

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(22), dp(20), dp(30));
        scroll.addView(root);

        TextView back = new TextView(this);
        back.setText("←  BACK");
        back.setTextColor(GOLD);
        back.setTextSize(14);
        back.setTypeface(null, Typeface.BOLD);
        back.setPadding(0, 0, 0, dp(20));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        addCover(root);

        nameView = new TextView(this);
        nameView.setText(artistName);
        nameView.setTextColor(WHITE);
        nameView.setTextSize(27);
        nameView.setTypeface(null, Typeface.BOLD);
        nameView.setPadding(0, dp(18), 0, dp(5));
        root.addView(nameView);

        statusView = new TextView(this);
        statusView.setText("ARTIST PROFILE");
        statusView.setTextColor(GOLD);
        statusView.setTextSize(11);
        statusView.setTypeface(null, Typeface.BOLD);
        root.addView(statusView);

        locationView = new TextView(this);
        locationView.setText("📍 " + district + "  •  " + genre);
        locationView.setTextColor(MUTED);
        locationView.setTextSize(13);
        locationView.setPadding(0, dp(12), 0, dp(16));
        root.addView(locationView);

        addSection(root, "APPLICATION STATUS");

        LinearLayout statusCard = createCard();

        TextView statusTitle = new TextView(this);
        statusTitle.setText("Artist account review");
        statusTitle.setTextColor(WHITE);
        statusTitle.setTextSize(15);
        statusTitle.setTypeface(null, Typeface.BOLD);
        statusCard.addView(statusTitle);

        TextView statusDescription = new TextView(this);
        statusDescription.setText(
                "Your application status is shown below. " +
                "Artist verification requires a separate identity " +
                "review and approval."
        );
        statusDescription.setTextColor(MUTED);
        statusDescription.setTextSize(12);
        statusDescription.setPadding(0, dp(8), 0, dp(10));
        statusCard.addView(statusDescription);

        TextView applicationState = new TextView(this);
        applicationState.setText(applicationStatus.toUpperCase());
        applicationState.setTextColor(GOLD);
        applicationState.setTextSize(12);
        applicationState.setTypeface(null, Typeface.BOLD);
        statusCard.addView(applicationState);

        root.addView(statusCard);

        addSection(root, "ABOUT THE ARTIST");

        bioView = new TextView(this);
        bioView.setText(biography);
        bioView.setTextColor(MUTED);
        bioView.setTextSize(14);
        bioView.setLineSpacing(dp(4), 1f);
        bioView.setPadding(dp(15), dp(15), dp(15), dp(15));
        bioView.setBackground(background(CARD, 14));
        root.addView(bioView);

        addSection(root, "MUSIC RELEASES");

        LinearLayout releases = createCard();

        TextView empty = new TextView(this);
        empty.setText(
                "No music available yet.\n\n" +
                "Approved artist releases will appear here."
        );
        empty.setTextColor(MUTED);
        empty.setTextSize(13);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(dp(10), dp(20), dp(10), dp(20));
        releases.addView(empty);

        root.addView(releases);

        TextView footer = new TextView(this);
        footer.setText(
                "BASOTHO MUSIC. BASOTHO TALENT. BASOTHO PRIDE."
        );
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void loadArtistApplication() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            statusView.setText("SIGN IN REQUIRED");
            return;
        }

        firestore.collection("artistApplications")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing()) return;

                    if (!document.exists()) {
                        statusView.setText("NO ARTIST APPLICATION");
                        return;
                    }

                    String savedName =
                            document.getString("artistName");

                    String savedDistrict =
                            document.getString("district");

                    String savedGenre =
                            document.getString("genre");

                    String savedBiography =
                            document.getString("biography");

                    String savedStatus =
                            document.getString("status");

                    if (savedName != null &&
                            !savedName.trim().isEmpty()) {
                        artistName = savedName;
                    }

                    if (savedDistrict != null) {
                        district = savedDistrict;
                    }

                    if (savedGenre != null) {
                        genre = savedGenre;
                    }

                    if (savedBiography != null) {
                        biography = savedBiography;
                    }

                    if (savedStatus != null) {
                        applicationStatus = savedStatus;
                    }

                    updateProfileDisplay();
                })
                .addOnFailureListener(error -> {
                    android.util.Log.e(
                            "MminoHub",
                            "Artist application loading failed",
                            error
                    );

                    statusView.setText("APPLICATION UNAVAILABLE");
                });
    }

    private void updateProfileDisplay() {

        nameView.setText(artistName);

        locationView.setText(
                "📍 " + district + "  •  " + genre
        );

        bioView.setText(biography);

        String status = applicationStatus.toLowerCase();

        if (status.equals("approved")) {
            statusView.setText("✓ APPROVED ARTIST");
            statusView.setTextColor(GOLD);
        } else if (status.equals("rejected")) {
            statusView.setText("APPLICATION NOT APPROVED");
            statusView.setTextColor(MUTED);
        } else {
            statusView.setText("ARTIST PROFILE • PENDING REVIEW");
            statusView.setTextColor(GOLD);
        }
    }

    private void addCover(LinearLayout parent) {

        LinearLayout cover = new LinearLayout(this);
        cover.setGravity(Gravity.CENTER);
        cover.setOrientation(LinearLayout.VERTICAL);
        cover.setBackground(background(CARD, 18));

        TextView icon = new TextView(this);
        icon.setText("♪");
        icon.setTextColor(GOLD);
        icon.setTextSize(65);
        icon.setGravity(Gravity.CENTER);
        cover.addView(icon);

        TextView label = new TextView(this);
        label.setText("ARTIST PROFILE");
        label.setTextColor(MUTED);
        label.setTextSize(10);
        label.setTypeface(null, Typeface.BOLD);
        cover.addView(label);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(190));

        parent.addView(cover, params);
    }

    private void addSection(
            LinearLayout parent,
            String text
    ) {
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

    private LinearLayout createCard() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));
        return card;
    }

    private GradientDrawable background(
            int color,
            int radius
    ) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
