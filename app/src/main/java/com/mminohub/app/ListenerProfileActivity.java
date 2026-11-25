
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class ListenerProfileActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText nameInput;
    private EditText emailInput;
    private RadioGroup languageGroup;
    private TextView headerName;
    private Button editButton;
    private Button saveButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    private boolean editing = false;
    private boolean loading = false;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        buildScreen();
        loadProfile();
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
        title.setText("My Profile");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Your MminoHub listener account.");
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addProfileHeader(root);
        addSection(root, "PERSONAL INFORMATION");

        LinearLayout details = createCard();

        nameInput = addInput(details, "Full name");
        emailInput = addInput(details, "Email address");

        emailInput.setEnabled(false);
        emailInput.setFocusable(false);

        editButton = createButton("EDIT PROFILE");
        details.addView(editButton);

        saveButton = createButton("SAVE CHANGES");
        saveButton.setVisibility(View.GONE);
        details.addView(saveButton);

        editButton.setOnClickListener(v -> {
            if (currentUser == null) {
                showMessage("Please sign in first.");
                return;
            }

            editing = true;
            nameInput.setEnabled(true);
            nameInput.requestFocus();

            editButton.setVisibility(View.GONE);
            saveButton.setVisibility(View.VISIBLE);
        });

        saveButton.setOnClickListener(v -> saveProfile());

        root.addView(details);

        addSection(root, "LANGUAGE PREFERENCE");

        LinearLayout languageCard = createCard();

        TextView languageTitle = new TextView(this);
        languageTitle.setText("Choose your language");
        languageTitle.setTextColor(WHITE);
        languageTitle.setTextSize(14);
        languageTitle.setTypeface(null, Typeface.BOLD);
        languageCard.addView(languageTitle);

        languageGroup = new RadioGroup(this);
        languageGroup.setOrientation(LinearLayout.VERTICAL);

        addLanguageOption(languageGroup, "Sesotho", 1);
        addLanguageOption(languageGroup, "English", 2);

        String savedLanguage = getPreferences(MODE_PRIVATE)
                .getString("language", "Sesotho");

        languageGroup.check(
                savedLanguage.equals("English") ? 2 : 1
        );

        languageGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = checkedId == 2
                    ? "English" : "Sesotho";

            getPreferences(MODE_PRIVATE)
                    .edit()
                    .putString("language", selected)
                    .apply();

            showMessage("Language preference saved: " + selected);
        });

        languageCard.addView(languageGroup);

        TextView languageNote = new TextView(this);
        languageNote.setText(
                "Your language preference is saved on this device. " +
                "Full app translation will be connected during integration."
        );
        languageNote.setTextColor(MUTED);
        languageNote.setTextSize(11);
        languageNote.setPadding(0, dp(8), 0, 0);
        languageCard.addView(languageNote);

        root.addView(languageCard);

        addSection(root, "ACCOUNT");

        LinearLayout accountCard = createCard();

        addAccountInfo(accountCard, "Account type", "Listener");
        addAccountInfo(accountCard, "Platform", "MminoHub Lesotho");

        TextView status = new TextView(this);
        status.setText(currentUser == null
                ? "NOT SIGNED IN"
                : "FIREBASE ACCOUNT CONNECTED");
        status.setTextColor(GOLD);
        status.setTextSize(10);
        status.setTypeface(null, Typeface.BOLD);
        status.setPadding(0, dp(12), 0, 0);
        accountCard.addView(status);

        Button signOut = createButton("SIGN OUT");
        signOut.setBackground(background(
                Color.rgb(92, 39, 48), 12
        ));
        signOut.setTextColor(WHITE);

        signOut.setOnClickListener(v -> signOut());

        accountCard.addView(signOut);
        root.addView(accountCard);

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • BASOTHO MUSIC");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void loadProfile() {
        if (currentUser == null) {
            nameInput.setText("");
            emailInput.setText("");
            showMessage("Sign in to view your profile.");
            return;
        }

        emailInput.setText(currentUser.getEmail() == null
                ? "" : currentUser.getEmail());

        firestore.collection("users")
                .document(currentUser.getUid())
                .get()
                .addOnSuccessListener(document -> {
                    if (isFinishing()) return;

                    String name = "";

                    if (document.exists()) {
                        name = document.getString("name");
                    }

                    if (name == null || name.trim().isEmpty()) {
                        name = currentUser.getDisplayName();
                    }

                    if (name == null || name.trim().isEmpty()) {
                        name = "MminoHub Listener";
                    }

                    nameInput.setText(name);
                    headerName.setText(name);
                })
                .addOnFailureListener(error -> {
                    android.util.Log.e(
                            "MminoHub",
                            "Profile loading failed",
                            error
                    );

                    String fallback = currentUser.getDisplayName();

                    if (fallback == null || fallback.trim().isEmpty()) {
                        fallback = "MminoHub Listener";
                    }

                    nameInput.setText(fallback);
                    headerName.setText(fallback);

                    showMessage("Could not load saved profile.");
                });
    }

    private void saveProfile() {
        if (loading) return;

        if (currentUser == null) {
            showMessage("Please sign in first.");
            return;
        }

        String name = nameInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError("Enter your name");
            return;
        }

        if (name.length() < 2) {
            nameInput.setError("Name is too short");
            return;
        }

        loading = true;
        saveButton.setEnabled(false);
        saveButton.setText("SAVING...");

        String uid = currentUser.getUid();
        String email = currentUser.getEmail();

        firestore.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing()) return;

                    if (document.exists()) {
                        firestore.collection("users")
                                .document(uid)
                                .update("name", name)
                                .addOnSuccessListener(unused ->
                                        profileSaved(name))
                                .addOnFailureListener(this::saveFailed);

                    } else {
                        Map<String, Object> profile =
                                new HashMap<>();

                        profile.put("uid", uid);
                        profile.put("name", name);
                        profile.put("email",
                                email == null ? "" : email);
                        profile.put("role", "listener");
                        profile.put(
                                "createdAt",
                                FieldValue.serverTimestamp()
                        );

                        firestore.collection("users")
                                .document(uid)
                                .set(profile)
                                .addOnSuccessListener(unused ->
                                        profileSaved(name))
                                .addOnFailureListener(this::saveFailed);
                    }
                })
                .addOnFailureListener(this::saveFailed);
    }

    private void profileSaved(String name) {
        loading = false;
        editing = false;

        nameInput.setText(name);
        nameInput.setEnabled(false);

        headerName.setText(name);

        saveButton.setEnabled(true);
        saveButton.setText("SAVE CHANGES");
        saveButton.setVisibility(View.GONE);
        editButton.setVisibility(View.VISIBLE);

        showMessage("Your profile has been saved.");
    }

    private void saveFailed(Exception error) {
        loading = false;

        saveButton.setEnabled(true);
        saveButton.setText("SAVE CHANGES");

        android.util.Log.e(
                "MminoHub",
                "Profile save failed",
                error
        );

        showMessage("Profile could not be saved. Check your connection and Firestore rules.");
    }

    private void signOut() {
        if (auth.getCurrentUser() == null) {
            showMessage("You are already signed out.");
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Sign out")
                .setMessage("Are you sure you want to sign out?")
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("SIGN OUT", (dialog, which) -> {
                    auth.signOut();
                    showMessage("You have signed out.");
                    finish();
                })
                .show();
    }

    private void addProfileHeader(LinearLayout parent) {
        LinearLayout card = createCard();
        card.setGravity(Gravity.CENTER);

        TextView avatar = new TextView(this);
        avatar.setText("♪");
        avatar.setTextColor(BG);
        avatar.setTextSize(32);
        avatar.setTypeface(null, Typeface.BOLD);
        avatar.setGravity(Gravity.CENTER);
        avatar.setBackground(background(GOLD, 50));

        LinearLayout.LayoutParams avatarParams =
                new LinearLayout.LayoutParams(dp(75), dp(75));

        card.addView(avatar, avatarParams);

        headerName = new TextView(this);
        headerName.setText("MminoHub Listener");
        headerName.setTextColor(WHITE);
        headerName.setTextSize(18);
        headerName.setTypeface(null, Typeface.BOLD);
        headerName.setPadding(0, dp(12), 0, dp(4));
        card.addView(headerName);

        TextView type = new TextView(this);
        type.setText("LISTENER PROFILE");
        type.setTextColor(GOLD);
        type.setTextSize(10);
        type.setTypeface(null, Typeface.BOLD);
        card.addView(type);

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

        params.topMargin = dp(23);
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

    private EditText addInput(
            LinearLayout parent,
            String hint
    ) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setHintTextColor(MUTED);
        input.setTextColor(WHITE);
        input.setTextSize(14);
        input.setSingleLine(true);
        input.setPadding(dp(12), 0, dp(12), 0);
        input.setBackground(background(BG, 10));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(48));

        params.bottomMargin = dp(12);
        parent.addView(input, params);

        return input;
    }

    private Button createButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setTextColor(BG);
        button.setTextSize(12);
        button.setTypeface(null, Typeface.BOLD);
        button.setBackground(background(GOLD, 12));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(47));

        params.topMargin = dp(8);
        button.setLayoutParams(params);

        return button;
    }

    private void addLanguageOption(
            RadioGroup group,
            String label,
            int id
    ) {
        RadioButton radio = new RadioButton(this);
        radio.setId(id);
        radio.setText(label);
        radio.setTextColor(WHITE);
        radio.setTextSize(13);
        radio.setButtonTintList(
                ColorStateList.valueOf(GOLD)
        );
        group.addView(radio);
    }

    private void addAccountInfo(
            LinearLayout parent,
            String label,
            String value
    ) {
        TextView info = new TextView(this);
        info.setText(label + ": " + value);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(5), 0, dp(5));
        parent.addView(info);
    }

    private void showMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
