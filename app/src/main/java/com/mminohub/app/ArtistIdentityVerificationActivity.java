
package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ArtistIdentityVerificationActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private CheckBox consent;

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
        back.setPadding(0, 0, 0, dp(22));
        back.setOnClickListener(v -> finish());
        root.addView(back);

        TextView title = new TextView(this);
        title.setText("Identity Verification");
        title.setTextColor(WHITE);
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Protecting Basotho artists and the MminoHub community."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(14);
        subtitle.setPadding(0, dp(8), 0, dp(22));
        root.addView(subtitle);

        addStatusCard(root);

        addSection(root, "01  REQUIRED IDENTITY DOCUMENT");

        addRequirement(
                root,
                "Lesotho National ID",
                "A valid Lesotho national identity document. " +
                "The document must be clear and readable."
        );

        addSection(root, "02  FACE VERIFICATION");

        addRequirement(
                root,
                "Clear selfie photograph",
                "A recent photograph of your face, taken in good " +
                "lighting without face coverings."
        );

        addSection(root, "03  VERIFICATION PROCESS");

        addRequirement(
                root,
                "Document review",
                "Our authorised verification team will check " +
                "the identity document."
        );

        addRequirement(
                root,
                "Identity confirmation",
                "The selfie and identity information will be " +
                "checked through a secure verification process."
        );

        addSection(root, "04  PRIVACY AND CONSENT");

        TextView privacy = new TextView(this);
        privacy.setText(
                "Your identity information is sensitive. MminoHub " +
                "must use secure transmission and protected storage, " +
                "restrict staff access and explain how long documents " +
                "are retained before collecting them. Identity " +
                "documents must never be sent through public links " +
                "or stored in the app's ordinary local storage."
        );
        privacy.setTextColor(MUTED);
        privacy.setTextSize(13);
        privacy.setPadding(dp(15), dp(15), dp(15), dp(15));
        privacy.setBackground(background(CARD, 14));
        root.addView(privacy);

        consent = new CheckBox(this);
        consent.setText(
                "I understand that MminoHub requires identity " +
                "verification before approving my artist account."
        );
        consent.setTextColor(WHITE);
        consent.setTextSize(13);
        consent.setButtonTintList(
                android.content.res.ColorStateList.valueOf(GOLD)
        );
        consent.setPadding(0, dp(15), 0, dp(15));
        root.addView(consent);

        Button continueButton = new Button(this);
        continueButton.setText("CONTINUE");
        continueButton.setTextColor(BG);
        continueButton.setTextSize(14);
        continueButton.setTypeface(null, Typeface.BOLD);
        continueButton.setBackground(background(GOLD, 14));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(54));
        buttonParams.topMargin = dp(15);
        root.addView(continueButton, buttonParams);

        continueButton.setOnClickListener(v -> continueVerification());

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • ARTIST PROTECTION");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private void addStatusCard(LinearLayout parent) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackground(background(CARD, 14));

        TextView heading = new TextView(this);
        heading.setText("VERIFICATION STATUS");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView status = new TextView(this);
        status.setText("NOT SUBMITTED");
        status.setTextColor(WHITE);
        status.setTextSize(20);
        status.setTypeface(null, Typeface.BOLD);
        status.setPadding(0, dp(8), 0, dp(5));
        card.addView(status);

        TextView description = new TextView(this);
        description.setText(
                "Your identity documents have not yet been collected."
        );
        description.setTextColor(MUTED);
        description.setTextSize(12);
        card.addView(description);

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

    private void addRequirement(
            LinearLayout parent,
            String headingText,
            String descriptionText
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(14), dp(15), dp(14));
        card.setBackground(background(CARD, 12));

        TextView heading = new TextView(this);
        heading.setText(headingText);
        heading.setTextColor(WHITE);
        heading.setTextSize(15);
        heading.setTypeface(null, Typeface.BOLD);
        card.addView(heading);

        TextView description = new TextView(this);
        description.setText(descriptionText);
        description.setTextColor(MUTED);
        description.setTextSize(12);
        description.setPadding(0, dp(7), 0, 0);
        card.addView(description);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(10);

        parent.addView(card, params);
    }

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }

    private void continueVerification() {
        if (!consent.isChecked()) {
            Toast.makeText(
                    this,
                    "Please confirm that you understand the verification process.",
                    Toast.LENGTH_LONG
            ).show();
            return;
        }

        Toast.makeText(
                this,
                "Identity verification requirements acknowledged. " +
                "Secure document submission will be enabled " +
                "when the backend is ready.",
                Toast.LENGTH_LONG
        ).show();
    }
}
