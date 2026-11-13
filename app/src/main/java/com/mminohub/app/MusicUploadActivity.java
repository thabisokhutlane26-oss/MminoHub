package com.mminohub.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class MusicUploadActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private static final int PICK_AUDIO = 501;
    private static final int PICK_COVER = 502;

    private EditText songTitle;
    private EditText artistName;
    private EditText albumName;
    private EditText description;

    private Spinner genre;

    private TextView audioStatus;
    private TextView coverStatus;

    private Uri selectedAudio;
    private Uri selectedCover;

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

        addSection(root, "01  MUSIC FILE");

        audioStatus = addFileCard(
                root,
                "Select audio file",
                "Choose an MP3 or supported audio file",
                "CHOOSE AUDIO",
                PICK_AUDIO
        );

        addSection(root, "02  COVER ARTWORK");

        coverStatus = addFileCard(
                root,
                "Select cover artwork",
                "Choose your release artwork",
                "CHOOSE COVER",
                PICK_COVER
        );

        addSection(root, "03  RELEASE INFORMATION");

        songTitle = addField(
                root,
                "Song title",
                "Enter the track title"
        );

        artistName = addField(
                root,
                "Artist name",
                "Enter your approved artist name"
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

        addSection(root, "04  SUBMISSION");

        TextView notice = new TextView(this);
        notice.setText(
                "Your release will need an administrative review " +
                "before it can appear in the public MminoHub catalogue. " +
                "Only upload music and artwork that you own or are " +
                "authorised to distribute."
        );
        notice.setTextColor(MUTED);
        notice.setTextSize(12);
        notice.setPadding(dp(15), dp(15), dp(15), dp(15));
        notice.setBackground(background(CARD, 14));
        root.addView(notice);

        Button submit = new Button(this);
        submit.setText("SUBMIT RELEASE");
        submit.setTextColor(BG);
        submit.setTextSize(14);
        submit.setTypeface(null, Typeface.BOLD);
        submit.setBackground(background(GOLD, 14));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(-1, dp(54));
        buttonParams.topMargin = dp(20);
        root.addView(submit, buttonParams);

        submit.setOnClickListener(v -> validateRelease());

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • MUSIC DISTRIBUTION");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(25), 0, dp(10));
        root.addView(footer);

        setContentView(scroll);
    }

    private TextView addFileCard(
            LinearLayout parent,
            String heading,
            String subtitle,
            String buttonText,
            int requestCode
    ) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(14), dp(15), dp(14));
        card.setBackground(background(CARD, 14));

        TextView name = new TextView(this);
        name.setText(heading);
        name.setTextColor(WHITE);
        name.setTextSize(15);
        name.setTypeface(null, Typeface.BOLD);
        card.addView(name);

        TextView info = new TextView(this);
        info.setText(subtitle);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(5), 0, dp(10));
        card.addView(info);

        TextView status = new TextView(this);
        status.setText("No file selected");
        status.setTextColor(GOLD);
        status.setTextSize(12);
        status.setPadding(0, 0, 0, dp(10));
        card.addView(status);

        Button choose = new Button(this);
        choose.setText(buttonText);
        choose.setTextColor(GOLD);
        choose.setTextSize(12);
        choose.setBackground(background(BG, 10));
        card.addView(choose);

        choose.setOnClickListener(v -> openFilePicker(requestCode));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);
        params.bottomMargin = dp(8);
        parent.addView(card, params);

        return status;
    }

    private void openFilePicker(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (requestCode == PICK_AUDIO) {
            intent.setType("audio/*");
        } else {
            intent.setType("image/*");
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "File picker could not be opened.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        Uri uri = data.getData();

        if (uri == null) {
            return;
        }

        if (requestCode == PICK_AUDIO) {
            selectedAudio = uri;
            audioStatus.setText("Audio file selected");
        } else if (requestCode == PICK_COVER) {
            selectedCover = uri;
            coverStatus.setText("Cover artwork selected");
        }
    }

    private void validateRelease() {

        if (selectedAudio == null) {
            showMessage("Please select an audio file.");
            return;
        }

        if (selectedCover == null) {
            showMessage("Please select cover artwork.");
            return;
        }

        if (songTitle.getText().toString().trim().isEmpty()) {
            songTitle.setError("Enter the song title");
            return;
        }

        if (artistName.getText().toString().trim().isEmpty()) {
            artistName.setError("Enter the artist name");
            return;
        }

        if (genre.getSelectedItemPosition() == 0) {
            showMessage("Please select a music genre.");
            return;
        }

        showMessage(
                "Release details completed. Actual upload and " +
                "administrative submission will be enabled " +
                "when the secure backend is ready."
        );
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
        field.setPadding(dp(14), dp(12), dp(14), dp(12));
        field.setBackground(background(CARD, 12));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, dp(52));
        params.bottomMargin = dp(8);

        parent.addView(field, params);
        return field;
    }

    private void addLabel(LinearLayout parent, String text) {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
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

    private void addSection(LinearLayout parent, String text) {
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

    private GradientDrawable background(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radius));
        drawable.setStroke(dp(1), Color.rgb(49, 62, 77));
        return drawable;
    }

    private void showMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
