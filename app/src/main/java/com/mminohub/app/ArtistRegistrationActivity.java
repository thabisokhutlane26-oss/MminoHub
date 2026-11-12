
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ArtistRegistrationActivity extends Activity {

    private final int BG = Color.rgb(10, 15, 25);
    private final int CARD = Color.rgb(21, 30, 43);
    private final int GOLD = Color.rgb(217, 164, 65);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 180, 195);

    private EditText realNameInput;
    private EditText artistNameInput;
    private EditText emailInput;
    private EditText phoneInput;
    private EditText locationInput;
    private EditText biographyInput;
    private Spinner genreSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showRegistration();
    }

    private int dp(int value) {
        return (int) (value *
                getResources().getDisplayMetrics().density);
    }

    private TextView text(
            String value,
            int size,
            int color,
            boolean bold
    ) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(null, Typeface.BOLD);
        }

        return t;
    }

    private void showRegistration() {

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(30));

        scroll.addView(root);

        TextView back = text(
                "← Back", 15, GOLD, true
        );

        back.setPadding(0, 0, 0, dp(20));

        back.setOnClickListener(v -> finish());

        root.addView(back);

        TextView title = text(
                "Become a MminoHub Artist",
                25, GOLD, true
        );

        root.addView(title);

        TextView subtitle = text(
                "Join the home of Basotho music and share your talent with Lesotho.",
                14, MUTED, false
        );

        subtitle.setPadding(0, dp(10), 0, dp(25));

        root.addView(subtitle);

        addSectionTitle(root, "PERSONAL INFORMATION");

        realNameInput = addInput(
                root, "Full legal name"
        );

        artistNameInput = addInput(
                root, "Artist / Stage name"
        );

        emailInput = addInput(
                root, "Email address"
        );

        emailInput.setInputType(
                android.text.InputType.TYPE_CLASS_TEXT
                        | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        phoneInput = addInput(
                root, "Phone number"
        );

        phoneInput.setInputType(
                android.text.InputType.TYPE_CLASS_PHONE
        );

        locationInput = addInput(
                root, "Town / District in Lesotho"
        );

        addSectionTitle(root, "MUSIC INFORMATION");

        TextView genreLabel = text(
                "Music genre", 13, MUTED, true
        );

        genreLabel.setPadding(0, dp(10), 0, dp(7));

        root.addView(genreLabel);

        genreSpinner = new Spinner(this);

        String[] genres = {
                "Select your genre",
                "Famo",
                "Traditional Sesotho",
                "Afro Pop",
                "Hip Hop",
                "Gospel",
                "R&B",
                "Maskandi",
                "House",
                "Other"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        genres
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        genreSpinner.setAdapter(adapter);
        genreSpinner.setBackgroundColor(CARD);

        root.addView(genreSpinner,
                new LinearLayout.LayoutParams(
                        -1, dp(50)
                ));

        TextView bioLabel = text(
                "Artist biography", 13, MUTED, true
        );

        bioLabel.setPadding(0, dp(18), 0, dp(7));

        root.addView(bioLabel);

        biographyInput = addInput(
                root, "Tell listeners about yourself and your music"
        );

        biographyInput.setSingleLine(false);
        biographyInput.setMinLines(4);
        biographyInput.setGravity(Gravity.TOP);

        TextView note = text(
                "Your application will need admin approval before your artist profile becomes publicly available.",
                12, MUTED, false
        );

        note.setPadding(0, dp(18), 0, dp(18));

        root.addView(note);

        Button submit = new Button(this);
        submit.setText("SUBMIT ARTIST APPLICATION");
        submit.setTextColor(BG);
        submit.setTextSize(13);
        submit.setTypeface(null, Typeface.BOLD);
        submit.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );

        root.addView(submit, new LinearLayout.LayoutParams(
                -1, dp(55)
        ));

        submit.setOnClickListener(v ->
                submitApplication()
        );

        setContentView(scroll);
    }

    private void addSectionTitle(
            LinearLayout root,
            String value
    ) {

        TextView heading = text(
                value, 12, GOLD, true
        );

        heading.setPadding(0, dp(15), 0, dp(10));

        root.addView(heading);
    }

    private EditText addInput(
            LinearLayout root,
            String hint
    ) {

        EditText input = new EditText(this);

        input.setHint(hint);
        input.setTextColor(WHITE);
        input.setHintTextColor(MUTED);
        input.setTextSize(14);
        input.setSingleLine(true);
        input.setPadding(
                dp(14), dp(10), dp(14), dp(10)
        );

        input.setBackgroundColor(CARD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1, dp(52)
                );

        params.bottomMargin = dp(12);

        root.addView(input, params);

        return input;
    }

    private void submitApplication() {

        String realName =
                realNameInput.getText().toString().trim();

        String artistName =
                artistNameInput.getText().toString().trim();

        String email =
                emailInput.getText().toString().trim();

        String phone =
                phoneInput.getText().toString().trim();

        String location =
                locationInput.getText().toString().trim();

        String biography =
                biographyInput.getText().toString().trim();

        String genre =
                genreSpinner.getSelectedItem().toString();

        if (realName.isEmpty()
                || artistName.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || location.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please complete all required fields.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {

            emailInput.setError("Enter a valid email address");
            return;
        }

        if (genre.equals("Select your genre")) {

            Toast.makeText(
                    this,
                    "Please select your music genre.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Toast.makeText(
                this,
                "Registration form completed. Application submission will be connected to the MminoHub server next.",
                Toast.LENGTH_LONG
        ).show();
    }
}
