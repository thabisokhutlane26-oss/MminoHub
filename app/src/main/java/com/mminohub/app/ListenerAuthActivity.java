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
import android.widget.TextView;
import android.widget.Toast;

public class ListenerAuthActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText nameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;

    private CheckBox termsCheck;

    private TextView modeSwitch;
    private TextView heading;
    private TextView subtitle;
    private Button submitButton;

    private boolean registrationMode = true;
    private boolean passwordVisible = false;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildScreen();
        updateMode();
    }

    private void buildScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(28), dp(22), dp(30));

        scroll.addView(root);

        TextView back = new TextView(this);
        back.setText("←  BACK");
        back.setTextColor(GOLD);
        back.setTextSize(14);
        back.setTypeface(null, Typeface.BOLD);
        back.setPadding(0, 0, 0, dp(28));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        TextView logo = new TextView(this);
        logo.setText("MMINOHUB");
        logo.setTextColor(GOLD);
        logo.setTextSize(15);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setGravity(Gravity.CENTER);
        root.addView(logo);

        TextView tagline = new TextView(this);
        tagline.setText(
                "Basotho Music. Basotho Talent. Basotho Pride."
        );
        tagline.setTextColor(MUTED);
        tagline.setTextSize(10);
        tagline.setGravity(Gravity.CENTER);
        tagline.setPadding(0, dp(7), 0, dp(25));
        root.addView(tagline);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18), dp(20), dp(18), dp(20));
        card.setBackground(background(CARD, 16));
        root.addView(card);

        heading = new TextView(this);
        heading.setTextColor(WHITE);
        heading.setTextSize(24);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        subtitle = new TextView(this);
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(12);
        subtitle.setPadding(0, dp(7), 0, dp(20));
        card.addView(subtitle);

        nameInput = addInput(
                card,
                "Full name",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS
        );

        emailInput = addInput(
                card,
                "Email address",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        passwordInput = addInput(
                card,
                "Password",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        TextView showPassword = new TextView(this);
        showPassword.setText("SHOW / HIDE PASSWORD");
        showPassword.setTextColor(GOLD);
        showPassword.setTextSize(11);
        showPassword.setTypeface(null, Typeface.BOLD);
        showPassword.setPadding(0, 0, 0, dp(15));
        showPassword.setOnClickListener(v -> togglePassword());
        card.addView(showPassword);

        confirmPasswordInput = addInput(
                card,
                "Confirm password",
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        termsCheck = new CheckBox(this);
        termsCheck.setText(
                "I agree to the MminoHub terms and privacy policy."
        );
        termsCheck.setTextColor(MUTED);
        termsCheck.setTextSize(11);
        termsCheck.setButtonTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );
        card.addView(termsCheck);

        submitButton = new Button(this);
        submitButton.setTextColor(Color.rgb(8, 17, 30));
        submitButton.setTextSize(14);
        submitButton.setTypeface(null, Typeface.BOLD);
        submitButton.setBackground(background(GOLD, 12));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(50));

        buttonParams.topMargin = dp(18);
        card.addView(submitButton, buttonParams);

        modeSwitch = new TextView(this);
        modeSwitch.setTextColor(GOLD);
        modeSwitch.setTextSize(12);
        modeSwitch.setGravity(Gravity.CENTER);
        modeSwitch.setPadding(0, dp(22), 0, dp(5));
        modeSwitch.setOnClickListener(v -> {
            registrationMode = !registrationMode;
            updateMode();
        });

        root.addView(modeSwitch);

        TextView notice = new TextView(this);
        notice.setText(
                "Account creation and secure login will be activated " +
                "when MminoHub's authentication backend is connected. " +
                "This screen currently validates information locally only."
        );
        notice.setTextColor(MUTED);
        notice.setTextSize(11);
        notice.setGravity(Gravity.CENTER);
        notice.setPadding(dp(8), dp(22), dp(8), 0);
        root.addView(notice);

        TextView footer = new TextView(this);
        footer.setText("MADE FOR BASOTHO MUSIC");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));
        root.addView(footer);

        submitButton.setOnClickListener(v -> validateForm());

        setContentView(scroll);
    }

    private EditText addInput(
            LinearLayout parent,
            String hint,
            int inputType
    ) {
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setHintTextColor(MUTED);
        input.setTextColor(WHITE);
        input.setTextSize(14);
        input.setSingleLine(true);
        input.setInputType(inputType);
        input.setPadding(dp(13), 0, dp(13), 0);
        input.setBackground(background(BG, 10));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(49));

        params.bottomMargin = dp(13);

        parent.addView(input, params);

        return input;
    }

    private void updateMode() {
        if (heading == null) return;

        if (registrationMode) {
            heading.setText("Create account");
            subtitle.setText(
                    "Join the MminoHub listener community."
            );

            nameInput.setVisibility(View.VISIBLE);
            confirmPasswordInput.setVisibility(View.VISIBLE);
            termsCheck.setVisibility(View.VISIBLE);

            submitButton.setText("CREATE ACCOUNT");

            modeSwitch.setText(
                    "Already have an account? Log in"
            );

        } else {
            heading.setText("Welcome back");
            subtitle.setText(
                    "Log in to continue enjoying Basotho music."
            );

            nameInput.setVisibility(View.GONE);
            confirmPasswordInput.setVisibility(View.GONE);
            termsCheck.setVisibility(View.GONE);

            submitButton.setText("LOG IN");

            modeSwitch.setText(
                    "New to MminoHub? Create an account"
            );
        }
    }

    private void togglePassword() {
        passwordVisible = !passwordVisible;

        int type = InputType.TYPE_CLASS_TEXT;

        if (!passwordVisible) {
            type |= InputType.TYPE_TEXT_VARIATION_PASSWORD;
        } else {
            type |= InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
        }

        passwordInput.setInputType(type);
        passwordInput.setSelection(
                passwordInput.getText().length()
        );

        confirmPasswordInput.setInputType(type);
        confirmPasswordInput.setSelection(
                confirmPasswordInput.getText().length()
        );
    }

    private void validateForm() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirm = confirmPasswordInput.getText().toString();

        if (registrationMode && name.isEmpty()) {
            nameInput.setError("Enter your full name");
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS
                .matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            return;
        }

        if (password.length() < 8) {
            passwordInput.setError(
                    "Password must have at least 8 characters"
            );
            return;
        }

        if (registrationMode && !password.equals(confirm)) {
            confirmPasswordInput.setError(
                    "Passwords do not match"
            );
            return;
        }

        if (registrationMode && !termsCheck.isChecked()) {
            Toast.makeText(
                    this,
                    "Please accept the terms and privacy policy.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        Toast.makeText(
                this,
                "Details validated. Secure account services " +
                "will be available after backend integration.",
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
