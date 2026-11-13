package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ArtistProfileActivity extends Activity {

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

        String artistName = getIntent().getStringExtra("artist_name");
        String district = getIntent().getStringExtra("artist_district");
        String genre = getIntent().getStringExtra("artist_genre");
        String biography = getIntent().getStringExtra("artist_biography");
        boolean verified = getIntent().getBooleanExtra(
                "artist_verified", false
        );

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

        addCover(root, artistName);

        TextView name = new TextView(this);
        name.setText(artistName);
        name.setTextColor(WHITE);
        name.setTextSize(27);
        name.setTypeface(null, Typeface.BOLD);
        name.setPadding(0, dp(18), 0, dp(5));
        root.addView(name);

        TextView verification = new TextView(this);
        verification.setText(
                verified ? "✓ VERIFIED ARTIST" : "ARTIST PROFILE"
        );
        verification.setTextColor(verified ? GOLD : MUTED);
        verification.setTextSize(11);
        verification.setTypeface(null, Typeface.BOLD);
        root.addView(verification);

        TextView location = new TextView(this);
        location.setText("📍 " + district + "  •  " + genre);
        location.setTextColor(MUTED);
        location.setTextSize(13);
        location.setPadding(0, dp(12), 0, dp(16));
        root.addView(location);

        Button follow = new Button(this);
        follow.setText("FOLLOW ARTIST");
        follow.setTextColor(BG);
        follow.setTextSize(13);
        follow.setTypeface(null, Typeface.BOLD);
        follow.setBackground(background(GOLD, 12));

        LinearLayout.LayoutParams followParams =
                new LinearLayout.LayoutParams(-1, dp(48));
        followParams.bottomMargin = dp(25);
        root.addView(follow, followParams);

        follow.setOnClickListener(v ->
                android.widget.Toast.makeText(
                        this,
                        "Artist following will be connected later.",
                        android.widget.Toast.LENGTH_SHORT
                ).show()
        );

        addSection(root, "ABOUT THE ARTIST");

        TextView bio = new TextView(this);
        bio.setText(biography);
        bio.setTextColor(MUTED);
        bio.setTextSize(14);
        bio.setLineSpacing(dp(4), 1f);
        bio.setPadding(dp(15), dp(15), dp(15), dp(15));
        bio.setBackground(background(CARD, 14));
        root.addView(bio);

        addSection(root, "MUSIC RELEASES");

        LinearLayout releases = new LinearLayout(this);
        releases.setOrientation(LinearLayout.VERTICAL);
        releases.setPadding(dp(16), dp(20), dp(16), dp(20));
        releases.setBackground(background(CARD, 14));

        TextView empty = new TextView(this);
        empty.setText(
                "No music available yet.\n\n" +
                "Check back soon as Basotho artists join MminoHub."
        );
        empty.setTextColor(MUTED);
        empty.setTextSize(13);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(dp(10), dp(15), dp(10), dp(15));
        releases.addView(empty);

        root.addView(releases);

        TextView footer = new TextView(this);
        footer.setText("BASOTHO MUSIC. BASOTHO TALENT. BASOTHO PRIDE.");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addCover(LinearLayout parent, String artistName) {
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

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
