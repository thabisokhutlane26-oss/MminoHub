package com.mminohub.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DiscoverActivity extends Activity {

    private final int BG = Color.rgb(10, 15, 25);
    private final int CARD = Color.rgb(21, 30, 43);
    private final int GOLD = Color.rgb(217, 164, 65);
    private final int WHITE = Color.WHITE;
    private final int MUTED = Color.rgb(170, 180, 195);

    private LinearLayout content;
    private EditText searchInput;

    private String selectedCategory = "All";

    private MusicCatalogue catalogue;
    private MusicPlaybackController playbackController;

    private final String[] categories = {
            "All",
            "New Releases",
            "Popular",
            "Albums",
            "Artists"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MminoHubRepository repository =
                MminoHubRepository.getInstance();

        catalogue = repository.getMusicCatalogue();

        playbackController =
                new MusicPlaybackController(this);

        showDiscover();
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

    private void showDiscover() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        TextView title = text(
                "Discover", 27, GOLD, true
        );

        title.setPadding(
                dp(20), dp(20), dp(20), dp(15)
        );

        root.addView(title);

        searchInput = new EditText(this);
        searchInput.setSingleLine(true);
        searchInput.setTextColor(WHITE);
        searchInput.setHintTextColor(MUTED);
        searchInput.setHint("Search songs, artists or albums");
        searchInput.setTextSize(14);
        searchInput.setPadding(
                dp(15), dp(12), dp(15), dp(12)
        );
        searchInput.setBackgroundColor(CARD);

        LinearLayout.LayoutParams searchParams =
                new LinearLayout.LayoutParams(-1, -2);

        searchParams.setMargins(
                dp(18), 0, dp(18), dp(15)
        );

        root.addView(searchInput, searchParams);

        HorizontalScrollView categoryScroll =
                new HorizontalScrollView(this);

        categoryScroll.setHorizontalScrollBarEnabled(false);

        LinearLayout categoryRow = new LinearLayout(this);
        categoryRow.setOrientation(LinearLayout.HORIZONTAL);
        categoryRow.setPadding(
                dp(18), 0, dp(18), dp(15)
        );

        for (String category : categories) {
            addCategoryButton(categoryRow, category);
        }

        categoryScroll.addView(categoryRow);
        root.addView(categoryScroll);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);

        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(18), dp(5), dp(18), dp(25)
        );

        scroll.addView(content);

        root.addView(scroll, new LinearLayout.LayoutParams(
                -1, 0, 1
        ));

        addBottomNavigation(root);

        setContentView(root);

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {
                renderResults();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        renderResults();
    }

    private void addCategoryButton(
            LinearLayout parent,
            String category
    ) {

        TextView button = text(
                category,
                12,
                category.equals(selectedCategory)
                        ? BG : WHITE,
                true
        );

        button.setGravity(Gravity.CENTER);
        button.setPadding(
                dp(15), dp(10), dp(15), dp(10)
        );

        button.setBackgroundColor(
                category.equals(selectedCategory)
                        ? GOLD : CARD
        );

        button.setClickable(true);
        button.setFocusable(true);

        button.setOnClickListener(v -> {

            selectedCategory = category;

            for (int i = 0; i < parent.getChildCount(); i++) {

                TextView item =
                        (TextView) parent.getChildAt(i);

                boolean selected =
                        item.getText().toString()
                                .equals(selectedCategory);

                item.setTextColor(
                        selected ? BG : WHITE
                );

                item.setBackgroundColor(
                        selected ? GOLD : CARD
                );
            }

            renderResults();
        });

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-2, -2);

        params.rightMargin = dp(8);

        parent.addView(button, params);
    }

    private void renderResults() {

        if (content == null) return;

        content.removeAllViews();

        String query = searchInput.getText()
                .toString()
                .trim()
                .toLowerCase(Locale.ROOT);

        List<MusicTrack> tracks =
                catalogue.getApprovedTracks();

        List<Artist> artists =
                catalogue.getApprovedArtists();

        if (selectedCategory.equals("Popular")) {

            showMessage(
                    "Popularity data is not available yet.",
                    "MminoHub will show popular music when listening statistics are available."
            );

            return;
        }

        if (selectedCategory.equals("New Releases")) {

            showMessage(
                    "Release dates are not available yet.",
                    "New releases will be organised here when release date information is available."
            );

            return;
        }

        if (selectedCategory.equals("Artists")) {

            showArtists(artists, query);
            return;
        }

        if (selectedCategory.equals("Albums")) {

            showAlbums(tracks, query);
            return;
        }

        boolean found = false;

        List<MusicTrack> matchingTracks =
                filterTracks(tracks, query);

        if (!matchingTracks.isEmpty()) {

            addHeading("Songs");

            for (MusicTrack track : matchingTracks) {
                addTrackCard(track, matchingTracks);
            }

            found = true;
        }

        if (selectedCategory.equals("All")) {

            List<Artist> matchingArtists =
                    filterArtists(artists, query);

            if (!matchingArtists.isEmpty()) {

                addHeading("Artists");

                for (Artist artist : matchingArtists) {
                    addArtistCard(artist);
                }

                found = true;
            }
        }

        if (!found) {
            showMessage(
                    query.isEmpty()
                            ? "No music available yet."
                            : "No matching results.",
                    query.isEmpty()
                            ? "Check back soon as Basotho artists join MminoHub."
                            : "Try another song, artist or album name."
            );
        }
    }

    private List<MusicTrack> filterTracks(
            List<MusicTrack> tracks,
            String query
    ) {

        List<MusicTrack> result = new ArrayList<>();

        for (MusicTrack track : tracks) {

            if (!track.isApproved()) continue;

            if (query.isEmpty()
                    || contains(track.getTitle(), query)
                    || contains(track.getArtist(), query)
                    || contains(track.getAlbum(), query)) {

                result.add(track);
            }
        }

        return result;
    }

    private List<Artist> filterArtists(
            List<Artist> artists,
            String query
    ) {

        List<Artist> result = new ArrayList<>();

        for (Artist artist : artists) {

            if (!artist.isApproved()) continue;

            if (query.isEmpty()
                    || contains(artist.getName(), query)
                    || contains(artist.getGenre(), query)
                    || contains(artist.getLocation(), query)) {

                result.add(artist);
            }
        }

        return result;
    }

    private boolean contains(
            String value,
            String query
    ) {

        return value != null
                && value.toLowerCase(Locale.ROOT)
                .contains(query);
    }

    private void showArtists(
            List<Artist> artists,
            String query
    ) {

        List<Artist> matching =
                filterArtists(artists, query);

        if (matching.isEmpty()) {

            showMessage(
                    "No artists available yet.",
                    "Basotho artists will appear here when they join MminoHub."
            );

            return;
        }

        addHeading("Basotho Artists");

        for (Artist artist : matching) {
            addArtistCard(artist);
        }
    }

    private void showAlbums(
            List<MusicTrack> tracks,
            String query
    ) {

        List<MusicTrack> matching =
                filterTracks(tracks, query);

        List<String> shownAlbums = new ArrayList<>();

        for (MusicTrack track : matching) {

            String album = track.getAlbum();

            if (album == null || album.trim().isEmpty()) {
                continue;
            }

            String key = album.toLowerCase(Locale.ROOT);

            if (shownAlbums.contains(key)) {
                continue;
            }

            shownAlbums.add(key);

            addAlbumCard(track, matching);
        }

        if (shownAlbums.isEmpty()) {

            showMessage(
                    "No albums available yet.",
                    "Approved albums will appear here when they are added to MminoHub."
            );
        }
    }

    private void addHeading(String value) {

        TextView heading = text(
                value, 18, WHITE, true
        );

        heading.setPadding(
                0, dp(15), 0, dp(12)
        );

        content.addView(heading);
    }

    private void addTrackCard(
            MusicTrack track,
            List<MusicTrack> queueTracks
    ) {

        LinearLayout card = createCard();

        TextView symbol = text(
                "♫", 25, GOLD, true
        );

        symbol.setGravity(Gravity.CENTER);

        card.addView(symbol, new LinearLayout.LayoutParams(
                dp(42), dp(45)
        ));

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);

        details.addView(text(
                safe(track.getTitle()),
                15, WHITE, true
        ));

        details.addView(text(
                safe(track.getArtist()),
                12, MUTED, false
        ));

        String extra = safe(track.getAlbum());

        if (track.isPremium()) {
            extra = extra.isEmpty()
                    ? "Premium"
                    : extra + "  •  Premium";
        }

        if (!extra.isEmpty()) {
            TextView info = text(
                    extra, 11, GOLD, false
            );

            info.setPadding(0, dp(4), 0, 0);

            details.addView(info);
        }

        card.addView(details, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        TextView play = text(
                "▶", 19, GOLD, true
        );

        play.setGravity(Gravity.CENTER);

        card.addView(play, new LinearLayout.LayoutParams(
                dp(38), dp(45)
        ));

        card.setClickable(true);
        card.setFocusable(true);

        card.setOnClickListener(v ->
                playSelectedTrack(track, queueTracks)
        );

        content.addView(card, cardParams());
    }

    private void playSelectedTrack(
            MusicTrack selectedTrack,
            List<MusicTrack> queueTracks
    ) {

        if (selectedTrack == null
                || queueTracks == null
                || queueTracks.isEmpty()) {

            Toast.makeText(
                    this,
                    "This track is not available.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int selectedIndex = -1;

        for (int i = 0; i < queueTracks.size(); i++) {

            MusicTrack item = queueTracks.get(i);

            if (item == selectedTrack
                    || (item.getId() != null
                    && item.getId().equals(selectedTrack.getId()))) {

                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex < 0) {

            Toast.makeText(
                    this,
                    "Could not find this track in the queue.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        PlaybackQueue queue =
                MminoHubRepository.getInstance()
                        .getPlaybackQueue();

        queue.setQueue(new ArrayList<>(queueTracks));

        try {

            playbackController.playTrack(selectedIndex);

            Intent intent = new Intent(
                    DiscoverActivity.this,
                    MusicPlayerActivity.class
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Unable to start playback. Please try again.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void addArtistCard(Artist artist) {

        LinearLayout card = createCard();

        TextView symbol = text(
                "♬", 25, GOLD, true
        );

        symbol.setGravity(Gravity.CENTER);

        card.addView(symbol, new LinearLayout.LayoutParams(
                dp(42), dp(45)
        ));

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);

        String name = safe(artist.getName());

        if (artist.isVerified()) {
            name += "  ✓";
        }

        details.addView(text(
                name, 15, WHITE, true
        ));

        String information = safe(artist.getGenre());

        if (!safe(artist.getLocation()).isEmpty()) {
            if (!information.isEmpty()) {
                information += "  •  ";
            }

            information += artist.getLocation();
        }

        details.addView(text(
                information.isEmpty()
                        ? "Basotho Artist"
                        : information,
                12, MUTED, false
        ));

        card.addView(details, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        content.addView(card, cardParams());
    }

    private void addAlbumCard(
            MusicTrack track,
            List<MusicTrack> albumTracks
    ) {

        LinearLayout card = createCard();

        TextView symbol = text(
                "▤", 25, GOLD, true
        );

        symbol.setGravity(Gravity.CENTER);

        card.addView(symbol, new LinearLayout.LayoutParams(
                dp(42), dp(45)
        ));

        LinearLayout details = new LinearLayout(this);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);

        details.addView(text(
                safe(track.getAlbum()),
                15, WHITE, true
        ));

        details.addView(text(
                safe(track.getArtist()),
                12, MUTED, false
        ));

        card.addView(details, new LinearLayout.LayoutParams(
                0, -2, 1
        ));

        TextView play = text(
                "▶", 18, GOLD, true
        );

        play.setGravity(Gravity.CENTER);

        card.addView(play, new LinearLayout.LayoutParams(
                dp(38), dp(45)
        ));

        card.setClickable(true);
        card.setFocusable(true);

        card.setOnClickListener(v ->
                playSelectedTrack(track, albumTracks)
        );

        content.addView(card, cardParams());
    }

    private LinearLayout createCard() {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(
                dp(14), dp(13), dp(14), dp(13)
        );
        card.setBackgroundColor(CARD);

        return card;
    }

    private LinearLayout.LayoutParams cardParams() {

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(-1, -2);

        params.bottomMargin = dp(9);

        return params;
    }

    private void showMessage(
            String title,
            String description
    ) {

        LinearLayout empty = new LinearLayout(this);
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(
                dp(20), dp(30), dp(20), dp(30)
        );
        empty.setBackgroundColor(CARD);

        TextView symbol = text(
                "♫", 38, GOLD, true
        );

        symbol.setGravity(Gravity.CENTER);

        TextView heading = text(
                title, 17, WHITE, true
        );

        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, dp(12), 0, dp(7));

        TextView explanation = text(
                description, 13, MUTED, false
        );

        explanation.setGravity(Gravity.CENTER);

        empty.addView(symbol);
        empty.addView(heading);
        empty.addView(explanation);

        content.addView(empty, cardParams());
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private void addBottomNavigation(LinearLayout root) {

        LinearLayout nav = new LinearLayout(this);
        nav.setGravity(Gravity.CENTER);
        nav.setBackgroundColor(CARD);
        nav.setPadding(0, dp(9), 0, dp(9));

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
                    destination.equals("Discover")
                            ? GOLD : MUTED,
                    destination.equals("Discover")
            );

            item.setGravity(Gravity.CENTER);
            item.setClickable(true);
            item.setFocusable(true);

            item.setOnClickListener(v -> {

                if (destination.equals("Discover")) {
                    return;
                }

                if (destination.equals("Home")) {
                    finish();
                    return;
                }

                Toast.makeText(
                        this,
                        destination + " will be connected next.",
                        Toast.LENGTH_SHORT
                ).show();
            });

            nav.addView(item, new LinearLayout.LayoutParams(
                    0, dp(48), 1
            ));
        }

        root.addView(nav);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (catalogue != null
                && content != null
                && searchInput != null) {

            renderResults();
        }
    }
}
