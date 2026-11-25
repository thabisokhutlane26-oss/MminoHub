package com.mminohub.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends Activity {

    private final int BG = Color.rgb(10, 15, 25);
    private final int CARD = Color.rgb(21, 30, 43);
    private final int GOLD = Color.rgb(217, 164, 65);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 180, 195);

    private LinearLayout root;
    private LinearLayout content;
    private MiniPlayerView miniPlayer;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private TextView welcomeTitle;
    private TextView welcomeSubtitle;

    private boolean firstResume = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        showHome();
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

    private void showHome() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(
                dp(20), dp(18), dp(20), dp(12)
        );

        TextView logo = text(
                "MminoHub", 27, GOLD, true
        );

        header.addView(logo, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        FirebaseUser currentUser = auth.getCurrentUser();

        TextView notification = text(
                "♧", 27, WHITE, false
        );

        notification.setClickable(true);
        notification.setOnClickListener(v ->
                openSection("notifications")
        );

        header.addView(notification);
        root.addView(header);

        TextView tagline = text(
                "BASOTHO MUSIC  •  BASOTHO PRIDE",
                11, MUTED, true
        );

        tagline.setPadding(
                dp(20), 0, dp(20), dp(18)
        );

        root.addView(tagline);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(18), dp(8), dp(18), dp(24)
        );

        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addWelcome(currentUser);

        addSection("Explore MminoHub");

        addCategory(
                "♫",
                "New Releases",
                "Discover the latest music from Lesotho",
                "releases"
        );

        addCategory(
                "▶",
                "Popular Music",
                "Find music listeners are enjoying",
                "popular"
        );

        addCategory(
                "♬",
                "Artists",
                "Meet Basotho musicians and groups",
                "artists"
        );

        addCategory(
                "▤",
                "Albums & EPs",
                "Explore complete music collections",
                "albums"
        );

        addSection("Your MminoHub");

        addCategory(
                "♡",
                "Your Library",
                "Your saved and purchased music",
                "library"
        );

        addCategory(
                "◎",
                "Following",
                "Artists you choose to follow",
                "following"
        );

        addSection("Listener Account");

        if (currentUser == null) {

            addCategory(
                    "●",
                    "Sign In / Register",
                    "Create or access your listener account",
                    "auth"
            );

        } else {

            addCategory(
                    "✓",
                    "Account Active",
                    "You are signed in to MminoHub",
                    "profile"
            );

            addCategory(
                    "☺",
                    "My Profile",
                    "Manage your listener profile",
                    "profile"
            );

            addCategory(
                    "↪",
                    "Log Out",
                    "Sign out of your MminoHub account",
                    "logout"
            );
        }

        addCategory(
                "♧",
                "Notifications",
                "Updates from MminoHub",
                "notifications"
        );

        addCategory(
                "★",
                "MminoHub Premium",
                "Explore Premium listening",
                "premium"
        );

        addCategory(
                "▣",
                "Purchase History",
                "View your music purchases",
                "purchases"
        );

        addCategory(
                "?",
                "Help & Support",
                "Get help using MminoHub",
                "support"
        );

        addSection("For Basotho Artists");

        addCategory(
                "♫",
                "Artist Registration",
                "Join MminoHub as an artist",
                "artist_register"
        );

        addCategory(
                "✓",
                "Identity Verification",
                "Review artist verification requirements",
                "artist_verify"
        );

        addCategory(
                "◉",
                "Artist Profile",
                "View an artist profile",
                "artist_profile"
        );

        addCategory(
                "+",
                "Upload Music",
                "Prepare a song or album release",
                "upload"
        );

        addCategory(
                "▤",
                "Release Management",
                "Manage your music releases",
                "release_management"
        );

        addCategory(
                "▦",
                "Artist Dashboard",
                "View your artist dashboard",
                "artist_dashboard"
        );

        addCategory(
                "M",
                "Artist Earnings",
                "View earnings information",
                "artist_earnings"
        );

        MusicTrack currentTrack = MminoHubRepository
                .getInstance()
                .getPlaybackQueue()
                .getCurrentTrack();

        miniPlayer = null;

        if (currentTrack != null) {
            miniPlayer = new MiniPlayerView(this);

            LinearLayout.LayoutParams playerParams =
                    new LinearLayout.LayoutParams(
                            -1, -2
                    );

            playerParams.setMargins(
                    dp(12), dp(8), dp(12), dp(8)
            );

            root.addView(miniPlayer, playerParams);
        }

        addBottomNavigation();

        setContentView(root);
    }

    private void addWelcome(FirebaseUser user) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(20), dp(22), dp(20), dp(22)
        );

        card.setBackgroundColor(CARD);

        if (user == null) {

            welcomeTitle = text(
                    "Discover the sound of Lesotho.",
                    25, WHITE, true
            );

            welcomeSubtitle = text(
                    "Explore Basotho artists, songs and albums in one place.",
                    14, MUTED, false
            );

        } else {

            welcomeTitle = text(
                    "Welcome back!",
                    25, GOLD, true
            );

            String email = user.getEmail();

            welcomeSubtitle = text(
                    email == null
                            ? "You are signed in to MminoHub."
                            : "Signed in as " + email,
                    14, MUTED, false
            );

            loadListenerName(user);
        }

        welcomeSubtitle.setPadding(0, dp(10), 0, 0);

        card.addView(welcomeTitle);
        card.addView(welcomeSubtitle);

        content.addView(card, new LinearLayout.LayoutParams(
                -1, -2
        ));
    }

    private void loadListenerName(FirebaseUser user) {

        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document -> {

                    if (isFinishing() || isDestroyed()) return;

                    if (auth.getCurrentUser() == null ||
                            !auth.getCurrentUser().getUid()
                                    .equals(user.getUid())) {
                        return;
                    }

                    if (document.exists()) {

                        String name = document.getString("name");

                        if (name != null && !name.trim().isEmpty()) {

                            welcomeTitle.setText(
                                    "Welcome back, " + name + "!"
                            );
                        }
                    }
                })
                .addOnFailureListener(error ->
                        android.util.Log.e(
                                "MminoHub",
                                "Could not load listener name",
                                error
                        )
                );
    }

    private void addSection(String title) {

        TextView heading = text(
                title, 19, WHITE, true
        );

        heading.setPadding(
                0, dp(24), 0, dp(12)
        );

        content.addView(heading);
    }

    private void addCategory(
            String symbol,
            String title,
            String description,
            String destination
    ) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(
                dp(15), dp(16), dp(15), dp(16)
        );

        card.setBackgroundColor(CARD);
        card.setClickable(true);
        card.setFocusable(true);

        TextView icon = text(
                symbol, 26, GOLD, true
        );

        icon.setGravity(Gravity.CENTER);

        card.addView(icon, new LinearLayout.LayoutParams(
                dp(45), dp(45)
        ));

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);

        details.addView(text(
                title, 16, WHITE, true
        ));

        TextView desc = text(
                description, 12, MUTED, false
        );

        desc.setPadding(0, dp(5), 0, 0);

        details.addView(desc);

        card.addView(details, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        card.setOnClickListener(v ->
                openSection(destination)
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(10);

        content.addView(card, params);
    }

    private void addBottomNavigation() {

        LinearLayout nav = new LinearLayout(this);
        nav.setBackgroundColor(CARD);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(0, dp(10), 0, dp(10));

        String[] labels = {
                "⌂\nHome",
                "⌕\nDiscover",
                "♫\nLibrary",
                "♡\nFollowing",
                "◎\nProfile"
        };

        for (int i = 0; i < labels.length; i++) {

            final String destination =
                    labels[i].substring(
                            labels[i].indexOf('\n') + 1
                    );

            TextView item = text(
                    labels[i],
                    11,
                    i == 0 ? GOLD : MUTED,
                    i == 0
            );

            item.setGravity(Gravity.CENTER);
            item.setClickable(true);
            item.setFocusable(true);

            item.setOnClickListener(v ->
                    openSection(destination)
            );

            nav.addView(item, new LinearLayout.LayoutParams(
                    0, dp(48), 1
            ));
        }

        root.addView(nav);
    }

    private void logout() {

        new AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage(
                        "Are you sure you want to log out of MminoHub?"
                )
                .setNegativeButton("CANCEL", null)
                .setPositiveButton("LOG OUT", (dialog, which) -> {

                    auth.signOut();

                    Toast.makeText(
                            this,
                            "You have been logged out.",
                            Toast.LENGTH_SHORT
                    ).show();

                    showHome();
                })
                .show();
    }

    private void openActivity(Class<?> activityClass) {

        Intent intent = new Intent(
                MainActivity.this,
                activityClass
        );

        startActivity(intent);
    }

    private void openSection(String destination) {

        switch (destination) {

            case "Home":
                showHome();
                return;

            case "Discover":
                openActivity(DiscoverActivity.class);
                return;

            case "library":
            case "Library":
                openActivity(ListenerLibraryActivity.class);
                return;

            case "following":
            case "Following":
                openActivity(ListenerFollowingActivity.class);
                return;

            case "Profile":
            case "profile":
                openActivity(ListenerProfileActivity.class);
                return;

            case "notifications":
                openActivity(ListenerNotificationsActivity.class);
                return;

            case "auth":
                openActivity(ListenerAuthActivity.class);
                return;

            case "logout":
                logout();
                return;

            case "premium":
                openActivity(ListenerPremiumActivity.class);
                return;

            case "purchases":
                openActivity(ListenerPurchaseHistoryActivity.class);
                return;

            case "support":
                openActivity(ListenerSupportActivity.class);
                return;

            case "artist_register":
                openActivity(ArtistRegistrationActivity.class);
                return;

            case "artist_verify":
                openActivity(ArtistIdentityVerificationActivity.class);
                return;

            case "artist_profile":
                openActivity(ArtistProfileActivity.class);
                return;

            case "upload":
                openActivity(MusicUploadActivity.class);
                return;

            case "release_management":
                openActivity(ReleaseManagementActivity.class);
                return;

            case "artist_dashboard":
                openActivity(ArtistDashboardActivity.class);
                return;

            case "artist_earnings":
                openActivity(ArtistEarningsActivity.class);
                return;

            case "releases":
            case "popular":
            case "albums":

                showMessage(
                        destination,
                        "No music available yet. Check back soon as Basotho artists join MminoHub."
                );
                return;

            case "artists":

                showMessage(
                        "Artists",
                        "No artists available yet. Basotho artists will appear here when they join MminoHub."
                );
                return;

            default:
                showMessage(
                        destination,
                        "This section is coming soon."
                );
        }
    }

    private void showMessage(String title, String message) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (firstResume) {
            firstResume = false;
        } else {
            showHome();
        }

        if (miniPlayer != null) {
            miniPlayer.refresh();
        }
    }
}
