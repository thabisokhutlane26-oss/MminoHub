
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

public class ArtistRegistrationActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText legalName;
    private EditText artistName;
    private EditText email;
    private EditText phone;
    private EditText town;
    private EditText biography;

    private Spinner district;
    private Spinner genre;

    private CheckBox idConfirmation;
    private Button submitButton;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FirebaseUser currentUser;

    private boolean submitting = false;

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
        title.setText("Artist Registration");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Join the home of Basotho music.");
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(14);
        subtitle.setPadding(0, dp(7), 0, dp(18));
        root.addView(subtitle);

        TextView accountNotice = new TextView(this);
        accountNotice.setText(currentUser == null
                ? "Sign in to your MminoHub listener account before submitting an artist application."
                : "Applying with: " + currentUser.getEmail());

        accountNotice.setTextColor(GOLD);
        accountNotice.setTextSize(12);
        accountNotice.setPadding(dp(14), dp(14), dp(14), dp(14));
        accountNotice.setBackground(background(CARD, 12));
        root.addView(accountNotice);

        addSection(root, "01  PERSONAL INFORMATION");

        legalName = addField(root,
                "Full legal name",
                "Enter your name as shown on your Lesotho ID");

        artistName = addField(root,
                "Artist / stage name",
                "Your public music name");

        email = addField(root,
                "Email address",
                "Your MminoHub account email");

        email.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        if (currentUser != null && currentUser.getEmail() != null) {
            email.setText(currentUser.getEmail());
        }

        email.setEnabled(false);

        phone = addField(root,
                "Phone number",
                "+266");

        phone.setInputType(InputType.TYPE_CLASS_PHONE);

        addSection(root, "02  LOCATION");

        town = addField(root,
                "Town or village",
                "Where are you based?");

        root.addView(makeLabel("District"));

        district = addSpinner(root, new String[]{
                "Select district",
                "Berea",
                "Butha-Buthe",
                "Leribe",
                "Mafeteng",
                "Maseru",
                "Mohale's Hoek",
                "Mokhotlong",
                "Qacha's Nek",
                "Quthing",
                "Thaba-Tseka"
        });

        addSection(root, "03  MUSIC INFORMATION");

        root.addView(makeLabel("Main music genre"));

        genre = addSpinner(root, new String[]{
                "Select music genre",
                "Famo",
                "Traditional Sesotho",
                "Afro Pop",
                "Hip Hop",
                "Gospel",
                "R&B",
                "Maskandi",
                "House",
                "Other"
        });

        biography = addField(root,
                "Artist biography",
                "Tell listeners about yourself and your music.");

        biography.setMinLines(4);
        biography.setGravity(Gravity.TOP);
        biography.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );

        biography.setSingleLine(false);
        biography.setHeight(dp(120));

        addSection(root, "04  IDENTITY REQUIREMENT");

        TextView identityInfo = new TextView(this);
        identityInfo.setText(
                "MminoHub requires a valid Lesotho national ID " +
                "for artist identity verification. Your ID " +
                "document and selfie will be collected later " +
                "through a secure verification process. " +
                "Do not send identity documents through this form."
        );

        identityInfo.setTextColor(MUTED);
        identityInfo.setTextSize(13);
        identityInfo.setPadding(dp(14), dp(14), dp(14), dp(14));
        identityInfo.setBackground(background(CARD, 14));
        root.addView(identityInfo);

        idConfirmation = new CheckBox(this);
        idConfirmation.setText(
                "I have a valid Lesotho national ID."
        );
        idConfirmation.setTextColor(WHITE);
        idConfirmation.setButtonTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );
        idConfirmation.setPadding(0, dp(12), 0, dp(12));
        root.addView(idConfirmation);

        submitButton = new Button(this);
        submitButton.setText("SUBMIT ARTIST APPLICATION");
        submitButton.setTextColor(BG);
        submitButton.setTextSize(13);
        submitButton.setTypeface(null, Typeface.BOLD);
        submitButton.setBackground(background(GOLD, 14));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(54));

        buttonParams.topMargin = dp(20);
        root.addView(submitButton, buttonParams);

        submitButton.setOnClickListener(v -> validateForm());

        TextView footer = new TextView(this);
        footer.setText(
                "BASOTHO MUSIC. BASOTHO TALENT. BASOTHO PRIDE."
        );
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void validateForm() {

        if (currentUser == null) {
            showMessage("Please sign in to your MminoHub account first.");
            return;
        }

        String legal = legalName.getText().toString().trim();
        String stage = artistName.getText().toString().trim();
        String enteredEmail = email.getText().toString().trim();
        String accountEmail = currentUser.getEmail();
        String mobile = phone.getText().toString().trim();
        String location = town.getText().toString().trim();
        String bio = biography.getText().toString().trim();

        if (legal.length() < 2) {
            legalName.setError("Enter your full legal name");
            return;
        }

        if (stage.isEmpty()) {
            artistName.setError("Enter your artist name");
            return;
        }

        if (accountEmail == null ||
                !enteredEmail.equalsIgnoreCase(accountEmail)) {
            email.setError("Use your signed-in account email");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(enteredEmail).matches()) {
            email.setError("Invalid email address");
            return;
        }

        if (mobile.length() < 8) {
            phone.setError("Enter a valid phone number");
            return;
        }

        if (location.isEmpty()) {
            town.setError("Enter your town or village");
            return;
        }

        if (district.getSelectedItemPosition() == 0) {
            showMessage("Please select your district.");
            return;
        }

        if (genre.getSelectedItemPosition() == 0) {
            showMessage("Please select your music genre.");
            return;
        }

        if (bio.isEmpty()) {
            biography.setError("Write a short biography");
            return;
        }

        if (bio.length() > 1000) {
            biography.setError("Biography must be 1000 characters or less");
            return;
        }

        if (!idConfirmation.isChecked()) {
            showMessage("A valid Lesotho national ID is required.");
            return;
        }

        submitApplication();
    }

    private void submitApplication() {

        if (submitting || currentUser == null) {
            return;
        }

        submitting = true;
        submitButton.setEnabled(false);
        submitButton.setText("SUBMITTING...");

        String uid = currentUser.getUid();
        String accountEmail = currentUser.getEmail();

        firestore.collection("artistApplications")
                .document(uid)
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing()) return;

                    if (document.exists()) {
                        submitting = false;
                        submitButton.setEnabled(true);
                        submitButton.setText(
                                "SUBMIT ARTIST APPLICATION"
                        );

                        showMessage(
                                "An artist application already exists for this account."
                        );
                        return;
                    }

                    Map<String, Object> application =
                            new HashMap<>();

                    application.put("uid", uid);
                    application.put(
                            "legalName",
                            legalName.getText().toString().trim()
                    );
                    application.put(
                            "artistName",
                            artistName.getText().toString().trim()
                    );
                    application.put("email", accountEmail);
                    application.put(
                            "phone",
                            phone.getText().toString().trim()
                    );
                    application.put(
                            "town",
                            town.getText().toString().trim()
                    );
                    application.put(
                            "district",
                            district.getSelectedItem().toString()
                    );
                    application.put(
                            "genre",
                            genre.getSelectedItem().toString()
                    );
                    application.put(
                            "biography",
                            biography.getText().toString().trim()
                    );
                    application.put("status", "pending");
                    application.put(
                            "createdAt",
                            FieldValue.serverTimestamp()
                    );

                    firestore.collection("artistApplications")
                            .document(uid)
                            .set(application)
                            .addOnSuccessListener(unused -> {
                                submitting = false;

                                showMessage(
                                        "Artist application submitted! Status: Pending review."
                                );

                                finish();
                            })
                            .addOnFailureListener(this::submissionFailed);
                })
                .addOnFailureListener(this::submissionFailed);
    }

    private void submissionFailed(Exception error) {

        submitting = false;

        if (isFinishing()) return;

        submitButton.setEnabled(true);
        submitButton.setText("SUBMIT ARTIST APPLICATION");

        android.util.Log.e(
                "MminoHub",
                "Artist application submission failed",
                error
        );

        showMessage(
                "Application could not be submitted. Check your connection and Firestore rules."
        );
    }

    private void addSection(LinearLayout parent, String text) {
        TextView section = new TextView(this);
        section.setText(text);
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.topMargin = dp(22);
        params.bottomMargin = dp(13);

        parent.addView(section, params);
    }

    private TextView makeLabel(String text) {
        TextView label = new TextView(this);
        label.setText(text);
        label.setTextColor(WHITE);
        label.setTextSize(13);
        label.setTypeface(null, Typeface.BOLD);
        label.setPadding(0, dp(10), 0, dp(7));
        return label;
    }

    private EditText addField(
            LinearLayout parent,
            String labelText,
            String hint
    ) {
        parent.addView(makeLabel(labelText));

        EditText field = new EditText(this);
        field.setSingleLine(false);
        field.setTextColor(WHITE);
        field.setHintTextColor(MUTED);
        field.setHint(hint);
        field.setTextSize(14);
        field.setPadding(dp(14), dp(12), dp(14), dp(12));
        field.setBackground(background(CARD, 12));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));

        params.bottomMargin = dp(8);
        parent.addView(field, params);

        return field;
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

    private void showMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
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
