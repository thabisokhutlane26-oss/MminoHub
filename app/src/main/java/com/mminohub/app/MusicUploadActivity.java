package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MusicUploadActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int GREEN = Color.rgb(76, 210, 145);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText releaseTitle;
    private EditText description;
    private Spinner releaseType;

    private TextView artistNameView;
    private Button saveButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private String registeredArtistName = "";
    private boolean saving = false;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        buildScreen();
        loadArtistName();
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
        back.setPadding(0, 0, 0, dp(20));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        TextView title = new TextView(this);
        title.setText("Create Release");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Prepare your music release for the Basotho community."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addSection(root, "01  ARTIST ACCOUNT");

        LinearLayout artistCard = createCard();

        TextView artistLabel = new TextView(this);
        artistLabel.setText("REGISTERED ARTIST");
        artistLabel.setTextColor(GOLD);
        artistLabel.setTextSize(10);
        artistLabel.setTypeface(null, Typeface.BOLD);
        artistCard.addView(artistLabel);

        artistNameView = new TextView(this);
        artistNameView.setText("Loading artist profile...");
        artistNameView.setTextColor(WHITE);
        artistNameView.setTextSize(16);
        artistNameView.setTypeface(null, Typeface.BOLD);
        artistNameView.setPadding(0, dp(8), 0, 0);
        artistCard.addView(artistNameView);

        root.addView(artistCard);

        addSection(root, "02  RELEASE INFORMATION");

        addLabel(root, "Release title");

        releaseTitle = addField(
                root,
                "Enter your release title"
        );

        addLabel(root, "Release type");

        releaseType = addSpinner(root, new String[]{
                "Select release type",
                Release.TYPE_SINGLE,
                Release.TYPE_EP,
                Release.TYPE_ALBUM
        });

        addLabel(root, "Release description");

        description = addField(
                root,
                "Describe your music release"
        );

        description.setMinLines(4);
        description.setGravity(Gravity.TOP);
        description.setSingleLine(false);

        addSection(root, "03  DISTRIBUTION STATUS");

        LinearLayout statusCard = createCard();

        TextView status = new TextView(this);
        status.setText("DRAFT SAVING AVAILABLE");
        status.setTextColor(GREEN);
        status.setTextSize(14);
        status.setTypeface(null, Typeface.BOLD);
        statusCard.addView(status);

        TextView statusInfo = new TextView(this);
        statusInfo.setText(
                "You can save release information as a draft. " +
                "Audio uploads, artwork uploads and distribution " +
                "will become available when MminoHub's secure " +
                "distribution service is ready."
        );
        statusInfo.setTextColor(MUTED);
        statusInfo.setTextSize(12);
        statusInfo.setPadding(0, dp(9), 0, 0);
        statusCard.addView(statusInfo);

        root.addView(statusCard);

        addSection(root, "04  IMPORTANT");

        TextView notice = new TextView(this);
        notice.setText(
                "Saving a draft does not submit your music for review " +
                "and does not publish it. Only submit music and artwork " +
                "you own or are authorised to distribute."
        );
        notice.setTextColor(MUTED);
        notice.setTextSize(12);
        notice.setPadding(dp(15), dp(15), dp(15), dp(15));
        notice.setBackground(background(CARD, 14));
        root.addView(notice);

        saveButton = new Button(this);
        saveButton.setText("SAVE RELEASE DRAFT");
        saveButton.setTextColor(BG);
        saveButton.setTextSize(13);
        saveButton.setTypeface(null, Typeface.BOLD);
        saveButton.setBackground(background(GOLD, 14));
        saveButton.setOnClickListener(v -> saveDraft());

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(54));

        buttonParams.topMargin = dp(22);
        root.addView(saveButton, buttonParams);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • BASOTHO MUSIC DISTRIBUTION");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void loadArtistName() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            artistNameView.setText("SIGN IN REQUIRED");
            saveButton.setEnabled(false);
            saveButton.setAlpha(0.5f);
            return;
        }

        firestore.collection("artistApplications")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (!document.exists()) {
                        artistNameView.setText(
                                "ARTIST APPLICATION NOT FOUND"
                        );
                        saveButton.setEnabled(false);
                        saveButton.setAlpha(0.5f);
                        return;
                    }

                    registeredArtistName =
                            document.getString("artistName");

                    if (registeredArtistName == null
                            || registeredArtistName.trim().isEmpty()) {

                        registeredArtistName = "Registered Artist";
                    }

                    artistNameView.setText(registeredArtistName);
                    saveButton.setEnabled(true);
                    saveButton.setAlpha(1f);
                })
                .addOnFailureListener(error -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    artistNameView.setText(
                            "ARTIST PROFILE UNAVAILABLE"
                    );

                    saveButton.setEnabled(false);
                    saveButton.setAlpha(0.5f);

                    android.util.Log.e(
                            "MminoHub",
                            "Artist profile loading failed",
                            error
                    );
                });
    }

    private void saveDraft() {

        if (saving) {
            return;
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(
                    this,
                    "Please sign in first.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        if (registeredArtistName.trim().isEmpty()) {
            Toast.makeText(
                    this,
                    "Your artist profile is not ready.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        String title = releaseTitle.getText()
                .toString().trim();

        String releaseDescription = description.getText()
                .toString().trim();

        int selectedPosition = releaseType.getSelectedItemPosition();

        if (title.isEmpty()) {
            releaseTitle.setError("Enter a release title");
            releaseTitle.requestFocus();
            return;
        }

        if (selectedPosition == 0) {
            Toast.makeText(
                    this,
                    "Please select a release type.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String selectedType =
                releaseType.getSelectedItem().toString();

        if (!selectedType.equals(Release.TYPE_SINGLE)
                && !selectedType.equals(Release.TYPE_EP)
                && !selectedType.equals(Release.TYPE_ALBUM)) {

            Toast.makeText(
                    this,
                    "Invalid release type.",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String releaseId = UUID.randomUUID().toString();

        Map<String, Object> release = new HashMap<>();

        release.put("id", releaseId);
        release.put("artistId", user.getUid());
        release.put("title", title);
        release.put("description", releaseDescription);
        release.put("coverUrl", "");
        release.put("type", selectedType);
        release.put("status", Release.STATUS_DRAFT);
        release.put("releaseDate", System.currentTimeMillis());
        release.put("trackCount", 0);

        saving = true;
        saveButton.setEnabled(false);
        saveButton.setText("SAVING DRAFT...");

        firestore.collection("artistReleases")
                .document(releaseId)
                .set(release)
                .addOnSuccessListener(unused -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    saving = false;

                    Toast.makeText(
                            this,
                            "Release draft saved successfully!",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();
                })
                .addOnFailureListener(error -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    saving = false;
                    saveButton.setEnabled(true);
                    saveButton.setText("SAVE RELEASE DRAFT");

                    Toast.makeText(
                            this,
                            "Could not save draft. Check your connection and Firestore rules.",
                            Toast.LENGTH_LONG
                    ).show();

                    android.util.Log.e(
                            "MminoHub",
                            "Release draft save failed",
                            error
                    );
                });
    }

    private LinearLayout createCard() {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );
        card.setBackground(background(CARD, 14));

        return card;
    }

    private EditText addField(
            LinearLayout parent,
            String hint
    ) {

        EditText field = new EditText(this);
        field.setTextColor(WHITE);
        field.setHintTextColor(MUTED);
        field.setHint(hint);
        field.setTextSize(14);
        field.setPadding(
                dp(14),
                dp(12),
                dp(14),
                dp(12)
        );
        field.setBackground(background(CARD, 12));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.bottomMargin = dp(8);
        parent.addView(field, params);

        return field;
    }

    private void addLabel(
            LinearLayout parent,
            String text
    ) {

        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(WHITE);
        label.setTextSize(13);
        label.setTypeface(null, Typeface.BOLD);
        label.setPadding(0, dp(10), 0, dp(7));

        parent.addView(label);
    }

    private Spinner addSpinner(
            LinearLayout parent,
            String[] options
    ) {

        Spinner spinner = new Spinner(this);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        options
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);
        spinner.setPadding(dp(10), 0, dp(10), 0);
        spinner.setBackground(background(CARD, 12));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.bottomMargin = dp(8);
        parent.addView(spinner, params);

        return spinner;
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

        params.topMargin = dp(22);
        params.bottomMargin = dp(12);

        parent.addView(section, params);
    }

    private GradientDrawable background(
            int color,
            int radius
    ) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(
                dp(1),
                Color.rgb(49, 62, 77)
        );

        return drawable;
    }
}
