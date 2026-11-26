package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class MusicUploadActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int GREEN = Color.rgb(76, 210, 145);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private EditText songTitle;
    private EditText artistName;
    private EditText albumName;
    private EditText description;

    private Spinner genre;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

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
        title.setText("Upload Music");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Prepare your music for the Basotho community."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));
        root.addView(subtitle);

        addSection(root, "01  MUSIC DISTRIBUTION");

        LinearLayout statusCard = createCard();

        TextView status = new TextView(this);
        status.setText("UPLOADS COMING SOON");
        status.setTextColor(GREEN);
        status.setTextSize(16);
        status.setTypeface(null, Typeface.BOLD);
        statusCard.addView(status);

        TextView statusInfo = new TextView(this);
        statusInfo.setText(
                "MminoHub is preparing its secure music storage " +
                "and distribution system. Artist uploads will open " +
                "once the service is ready."
        );
        statusInfo.setTextColor(MUTED);
        statusInfo.setTextSize(13);
        statusInfo.setPadding(0, dp(10), 0, 0);
        statusCard.addView(statusInfo);

        root.addView(statusCard);

        addSection(root, "02  RELEASE INFORMATION");

        songTitle = addField(
                root,
                "Song title",
                "Enter the track title"
        );

        artistName = addField(
                root,
                "Artist name",
                "Your registered artist name"
        );

        albumName = addField(
                root,
                "Album or EP name",
                "Optional"
        );

        addLabel(root, "Music genre");

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

        description = addField(
                root,
                "Release description",
                "Tell listeners about this release"
        );

        description.setMinLines(4);
        description.setGravity(Gravity.TOP);
        description.setSingleLine(false);

        addSection(root, "03  RELEASE REQUIREMENTS");

        TextView notice = new TextView(this);
        notice.setText(
                "When uploads open, artists will be able to submit " +
                "audio files and cover artwork. Every release will " +
                "be reviewed before appearing in the public catalogue. " +
                "Only submit music and artwork you own or are " +
                "authorised to distribute."
        );
        notice.setTextColor(MUTED);
        notice.setTextSize(12);
        notice.setPadding(dp(15), dp(15), dp(15), dp(15));
        notice.setBackground(background(CARD, 14));
        root.addView(notice);

        TextView uploadStatus = new TextView(this);
        uploadStatus.setText(
                "Music distribution is not available yet."
        );
        uploadStatus.setTextColor(GOLD);
        uploadStatus.setTextSize(12);
        uploadStatus.setPadding(0, dp(18), 0, dp(8));
        root.addView(uploadStatus);

        Button submitButton = new Button(this);
        submitButton.setText("UPLOADS COMING SOON");
        submitButton.setTextColor(BG);
        submitButton.setTextSize(13);
        submitButton.setTypeface(null, Typeface.BOLD);
        submitButton.setBackground(background(GOLD, 14));
        submitButton.setEnabled(false);
        submitButton.setAlpha(0.8f);

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(54));

        buttonParams.topMargin = dp(10);
        root.addView(submitButton, buttonParams);

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
            return;
        }

        firestore.collection("artistApplications")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (document.exists()) {

                        String savedName =
                                document.getString("artistName");

                        if (savedName != null &&
                                !savedName.trim().isEmpty()) {

                            artistName.setText(savedName);
                        }
                    }
                })
                .addOnFailureListener(error ->
                        android.util.Log.e(
                                "MminoHub",
                                "Could not load artist name",
                                error
                        )
                );
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
            String label,
            String hint
    ) {

        addLabel(parent, label);

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
