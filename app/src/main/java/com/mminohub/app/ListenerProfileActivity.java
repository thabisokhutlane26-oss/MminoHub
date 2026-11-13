package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class ListenerProfileActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText nameInput;
    private EditText emailInput;
    private RadioGroup languageGroup;

    private boolean editing = false;

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

        nameInput.setEnabled(false);
        emailInput.setEnabled(false);

        Button editButton = createButton("EDIT PROFILE");
        details.addView(editButton);

        Button saveButton = createButton("SAVE CHANGES");
        saveButton.setVisibility(android.view.View.GONE);
        details.addView(saveButton);

        editButton.setOnClickListener(v -> {
            editing = true;
            nameInput.setEnabled(true);
            emailInput.setEnabled(true);
            editButton.setVisibility(android.view.View.GONE);
            saveButton.setVisibility(android.view.View.VISIBLE);
            nameInput.requestFocus();
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
        languageGroup.check(1);

        languageCard.addView(languageGroup);

        TextView languageNote = new TextView(this);
        languageNote.setText(
                "Language selection is currently a local preference. " +
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
        status.setText("AUTHENTICATION NOT CONNECTED");
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
        signOut.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "No active backend session to sign out from.",
                        Toast.LENGTH_LONG
                ).show()
        );

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

        TextView name = new TextView(this);
        name.setText("MminoHub Listener");
        name.setTextColor(WHITE);
        name.setTextSize(18);
        name.setTypeface(null, Typeface.BOLD);
        name.setPadding(0, dp(12), 0, dp(4));
        card.addView(name);

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

    private void saveProfile() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError("Enter your name");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            return;
        }

        nameInput.setEnabled(false);
        emailInput.setEnabled(false);
        editing = false;

        Toast.makeText(
                this,
                "Details checked. Profile saving will be enabled " +
                "after backend integration.",
                Toast.LENGTH_LONG
        ).show();
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
                android.content.res.ColorStateList.valueOf(GOLD)
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

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }
}
