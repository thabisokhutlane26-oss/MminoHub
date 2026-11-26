package com.mminohub.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Locale;

public class ListenerLibraryActivity extends Activity {

    private final int BG = Color.rgb(8, 17, 30);
    private final int CARD = Color.rgb(17, 31, 48);
    private final int GOLD = Color.rgb(232, 190, 91);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 181, 195);

    private LinearLayout root;

    private MminoHubRepository repository;
    private LibraryManager libraryManager;
    private PurchaseManager purchaseManager;

    private FirebaseAuth auth;

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        repository = MminoHubRepository.getInstance();
        libraryManager = repository.getLibraryManager();
        purchaseManager = repository.getPurchaseManager();

        buildScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (root != null) {
            refreshLibrary();
        }
    }

    private void buildScreen() {

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(
                dp(20), dp(24), dp(20), dp(30)
        );

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
        title.setText("My Music Library");
        title.setTextColor(WHITE);
        title.setTextSize(27);
        title.setTypeface(null, Typeface.BOLD);

        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText(
                "Your Basotho music collection."
        );
        subtitle.setTextColor(MUTED);
        subtitle.setTextSize(13);
        subtitle.setPadding(0, dp(7), 0, dp(22));

        root.addView(subtitle);

        refreshLibrary();

        setContentView(scroll);
    }

    private void refreshLibrary() {

        if (root == null) return;

        // Preserve the back button and heading.
        while (root.getChildCount() > 3) {
            root.removeViewAt(3);
        }

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            addOverview(0, 0, 0);

            addSection(root, "MY COLLECTION");

            addMessageCard(
                    "SIGN IN REQUIRED",
                    "Sign in to view your personal music library."
            );

            return;
        }

        String userId = user.getUid();

        List<String> savedIds =
                libraryManager.getSavedTrackIds();

        List<String> purchasedIds =
                libraryManager.getPurchasedTrackIds();

        List<MusicPurchase> purchases =
                purchaseManager.getUserPurchases(userId);

        List<Playlist> playlists =
                libraryManager.getPlaylists();

        int paidCount = 0;

        for (MusicPurchase purchase : purchases) {
            if (purchase.isPaid()) {
                paidCount++;
            }
        }

        addOverview(
                savedIds.size(),
                purchasedIds.size(),
                paidCount
        );

        // SAVED SONGS
        addSection(root, "SAVED SONGS");

        if (savedIds.isEmpty()) {
            addMessageCard(
                    "NO SAVED SONGS",
                    "Songs you save will appear here."
            );
        } else {
            for (String id : savedIds) {
                addTrackIdCard(
                        root,
                        "♫ SAVED TRACK",
                        id
                );
            }
        }

        // PURCHASED MUSIC
        addSection(root, "PURCHASED MUSIC");

        if (purchasedIds.isEmpty() && paidCount == 0) {
            addMessageCard(
                    "NO PURCHASES YET",
                    "Your purchased music will appear here."
            );
        } else {
            for (String id : purchasedIds) {
                addTrackIdCard(
                        root,
                        "OWNED TRACK",
                        id
                );
            }

            for (MusicPurchase purchase : purchases) {
                if (purchase.isPaid()) {
                    addPurchaseCard(purchase);
                }
            }
        }

        // DOWNLOADS
        addSection(root, "DOWNLOADED MUSIC");

        addMessageCard(
                "OFFLINE MUSIC NOT CONNECTED",
                "Downloaded audio files are not yet managed " +
                "by this library."
        );

        // PLAYLISTS
        addSection(root, "MY PLAYLISTS");

        addPlaylistSection(playlists);

        // INFORMATION
        addSection(root, "LIBRARY INFORMATION");

        addMessageCard(
                "YOUR MUSIC, YOUR COLLECTION",
                "Your current library manager keeps its data " +
                "in memory. Saved songs, playlists and purchase " +
                "records are not yet permanently synchronized " +
                "with your MminoHub account."
        );

        TextView footer = new TextView(this);
        footer.setText("MMINOHUB • YOUR MUSIC");
        footer.setTextColor(MUTED);
        footer.setTextSize(10);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(28), 0, dp(10));

        root.addView(footer);
    }

    private void addOverview(
            int saved,
            int purchased,
            int paid
    ) {

        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText("YOUR COLLECTION");
        heading.setTextColor(GOLD);
        heading.setTextSize(11);
        heading.setTypeface(null, Typeface.BOLD);

        card.addView(heading);

        TextView count = new TextView(this);
        count.setText(
                String.format(
                        Locale.getDefault(),
                        "%d saved • %d owned",
                        saved,
                        purchased
                )
        );
        count.setTextColor(WHITE);
        count.setTextSize(22);
        count.setTypeface(null, Typeface.BOLD);
        count.setPadding(0, dp(9), 0, dp(5));

        card.addView(count);

        TextView description = new TextView(this);
        description.setText(
                paid + " verified paid record(s) in current session."
        );
        description.setTextColor(MUTED);
        description.setTextSize(12);

        card.addView(description);

        root.addView(card);
    }

    private void addSection(
            LinearLayout parent,
            String label
    ) {

        TextView section = new TextView(this);
        section.setText(label);
        section.setTextColor(GOLD);
        section.setTextSize(12);
        section.setTypeface(null, Typeface.BOLD);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.topMargin = dp(24);
        params.bottomMargin = dp(12);

        parent.addView(section, params);
    }

    private void addMessageCard(
            String heading,
            String message
    ) {

        LinearLayout card = createCard();

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(WHITE);
        title.setTextSize(13);
        title.setTypeface(null, Typeface.BOLD);

        card.addView(title);

        TextView info = new TextView(this);
        info.setText(message);
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(7), 0, 0);

        card.addView(info);

        root.addView(card);
    }

    private void addTrackIdCard(
            LinearLayout parent,
            String heading,
            String trackId
    ) {

        LinearLayout card = createCard();

        TextView title = new TextView(this);
        title.setText(heading);
        title.setTextColor(GOLD);
        title.setTextSize(11);
        title.setTypeface(null, Typeface.BOLD);

        card.addView(title);

        TextView id = new TextView(this);
        id.setText("Track reference: " + trackId);
        id.setTextColor(WHITE);
        id.setTextSize(13);
        id.setPadding(0, dp(7), 0, 0);

        card.addView(id);

        parent.addView(card);
    }

    private void addPurchaseCard(MusicPurchase purchase) {

        LinearLayout card = createCard();

        TextView title = new TextView(this);
        title.setText("PAID PURCHASE");
        title.setTextColor(GOLD);
        title.setTextSize(11);
        title.setTypeface(null, Typeface.BOLD);

        card.addView(title);

        TextView track = new TextView(this);
        track.setText(
                "Track: " + purchase.getTrackId()
        );
        track.setTextColor(WHITE);
        track.setTextSize(13);
        track.setPadding(0, dp(7), 0, 0);

        card.addView(track);

        TextView amount = new TextView(this);
        amount.setText(
                String.format(
                        Locale.getDefault(),
                        "Amount: %s %.2f",
                        purchase.getCurrency() == null
                                ? ""
                                : purchase.getCurrency(),
                        purchase.getAmount()
                )
        );
        amount.setTextColor(MUTED);
        amount.setTextSize(12);
        amount.setPadding(0, dp(5), 0, 0);

        card.addView(amount);

        root.addView(card);
    }

    private void addPlaylistSection(
            List<Playlist> playlists
    ) {

        LinearLayout card = createCard();

        TextView heading = new TextView(this);
        heading.setText("Your playlists");
        heading.setTextColor(WHITE);
        heading.setTextSize(15);
        heading.setTypeface(null, Typeface.BOLD);

        card.addView(heading);

        TextView info = new TextView(this);
        info.setText(
                "Create collections of your favourite Basotho music."
        );
        info.setTextColor(MUTED);
        info.setTextSize(12);
        info.setPadding(0, dp(6), 0, dp(14));

        card.addView(info);

        if (playlists.isEmpty()) {

            TextView empty = new TextView(this);
            empty.setText("No playlists created yet.");
            empty.setTextColor(MUTED);
            empty.setTextSize(12);

            card.addView(empty);

        } else {

            for (Playlist playlist : playlists) {

                TextView item = new TextView(this);

                item.setText(
                        playlist.getName()
                                + " • "
                                + playlist.getTrackCount()
                                + " tracks"
                );

                item.setTextColor(WHITE);
                item.setTextSize(13);
                item.setPadding(
                        dp(10), dp(12), dp(10), dp(12)
                );
                item.setBackground(background(BG, 9));

                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(-1, -2);

                params.bottomMargin = dp(8);

                card.addView(item, params);

                item.setOnClickListener(v ->
                        showPlaylistDetails(playlist)
                );
            }
        }

        TextView create = new TextView(this);
        create.setText("+  CREATE PLAYLIST");
        create.setTextColor(GOLD);
        create.setTextSize(12);
        create.setTypeface(null, Typeface.BOLD);
        create.setPadding(0, dp(18), 0, dp(3));

        create.setOnClickListener(v ->
                showCreatePlaylistDialog()
        );

        card.addView(create);

        root.addView(card);
    }

    private void showCreatePlaylistDialog() {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            showMessage("Please sign in first.");
            return;
        }

        EditText nameInput = new EditText(this);
        nameInput.setSingleLine(true);
        nameInput.setHint("Playlist name");
        nameInput.setTextColor(WHITE);
        nameInput.setHintTextColor(MUTED);

        LinearLayout container = new LinearLayout(this);
        container.setPadding(
                dp(20), dp(8), dp(20), dp(4)
        );
        container.addView(nameInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create playlist")
                .setView(container)
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("CREATE", null)
                .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(
                    AlertDialog.BUTTON_POSITIVE
            ).setOnClickListener(v -> {

                String name = nameInput.getText()
                        .toString()
                        .trim();

                if (name.isEmpty()) {
                    nameInput.setError(
                            "Enter a playlist name"
                    );
                    return;
                }

                String id = "playlist_"
                        + System.currentTimeMillis();

                Playlist playlist = new Playlist(
                        id,
                        name,
                        user.getUid(),
                        "",
                        "",
                        false
                );

                boolean added =
                        libraryManager.addPlaylist(playlist);

                if (added) {
                    dialog.dismiss();
                    showMessage(
                            "Playlist created in this session."
                    );
                    refreshLibrary();
                } else {
                    showMessage(
                            "Could not create playlist."
                    );
                }
            });
        });

        dialog.show();
    }

    private void showPlaylistDetails(Playlist playlist) {

        String message =
                "Name: " + playlist.getName()
                + "\nTracks: " + playlist.getTrackCount()
                + "\nVisibility: "
                + (playlist.isPublicPlaylist()
                    ? "Public"
                    : "Private")
                + "\n\nPlaylist storage is currently local "
                + "to this app session.";

        new AlertDialog.Builder(this)
                .setTitle("Playlist details")
                .setMessage(message)
                .setPositiveButton("CLOSE", null)
                .setNegativeButton(
                        "DELETE",
                        (dialog, which) -> {

                            boolean removed =
                                    libraryManager.removePlaylist(
                                            playlist.getId()
                                    );

                            if (removed) {
                                showMessage("Playlist removed.");
                                refreshLibrary();
                            }
                        }
                )
                .show();
    }

    private LinearLayout createCard() {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(15), dp(15), dp(15), dp(15)
        );
        card.setBackground(background(CARD, 14));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(11);

        card.setLayoutParams(params);

        return card;
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

    private void showMessage(String message) {
        Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
        ).show();
    }
}
