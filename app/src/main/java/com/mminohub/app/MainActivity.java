
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        subtitle.setText(
                "Join the home of Basotho music."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(14);
        subtitle.setPadding(0, dp(7), 0, dp(24));
        root.addView(subtitle);

        addSection(root, "01  PERSONAL INFORMATION");

        legalName = addField(root,
                "Full legal name",
                "Enter your name as shown on your Lesotho ID");

        artistName = addField(root,
                "Artist / stage name",
                "Your public music name");

        email = addField(root,
                "Email address",
                "example@email.com");

        email.setInputType(33);

        phone = addField(root,
                "Phone number",
                "+266");

        phone.setInputType(3);

        addSection(root, "02  LOCATION");

        town = addField(root,
                "Town or village",
                "Where are you based?");

        TextView districtLabel = makeLabel("District");
        root.addView(districtLabel);

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

        TextView genreLabel = makeLabel("Main music genre");
        root.addView(genreLabel);

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
        biography.setInputType(147457);

        addSection(root, "04  IDENTITY REQUIREMENT");

        TextView identityInfo = new TextView(this);
        identityInfo.setText(
                "MminoHub requires a valid Lesotho national ID " +
                "for artist identity verification. Your ID " +
                "document and selfie will be collected later " +
                "through our secure verification process."
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

        Button submit = new Button(this);
        submit.setText("CONTINUE REGISTRATION");
        submit.setTextColor(BG);
        submit.setTextSize(14);
        submit.setTypeface(null, Typeface.BOLD);
        submit.setBackground(background(GOLD, 14));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1, dp(54)
                );
        buttonParams.topMargin = dp(20);
        root.addView(submit, buttonParams);

        submit.setOnClickListener(v -> validateForm());

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

    private void validateForm() {

        if (legalName.getText().toString().trim().isEmpty()) {
            legalName.setError("Enter your legal name");
            return;
        }

        if (artistName.getText().toString().trim().isEmpty()) {
            artistName.setError("Enter your artist name");
            return;
        }

        if (email.getText().toString().trim().isEmpty()) {
            email.setError("Enter your email");
            return;
        }

        if (phone.getText().toString().trim().isEmpty()) {
            phone.setError("Enter your phone number");
            return;
        }

        if (town.getText().toString().trim().isEmpty()) {
            town.setError("Enter your town or village");
            return;
        }

        if (district.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select your district",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (genre.getSelectedItemPosition() == 0) {
            Toast.makeText(
                    this,
                    "Please select your music genre",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (biography.getText().toString().trim().isEmpty()) {
            biography.setError("Write a short biography");
            return;
        }

        if (!idConfirmation.isChecked()) {
            Toast.makeText(
                    this,
                    "A valid Lesotho national ID is required",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        Toast.makeText(
                this,
                "Form completed. Secure registration submission " +
                "will be enabled when the MminoHub backend is ready.",
                Toast.LENGTH_LONG
        ).show();
    }
}
