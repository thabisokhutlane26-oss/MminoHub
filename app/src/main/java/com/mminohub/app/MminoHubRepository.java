package com.mminohub.app;

public class MminoHubRepository {

    private static MminoHubRepository instance;

    private final MusicCatalogue musicCatalogue;
    private final ReleaseManager releaseManager;
    private final AlbumManager albumManager;
    private final LibraryManager libraryManager;
    private final PurchaseManager purchaseManager;
    private final NotificationManager notificationManager;
    private final ArtistEarningsManager earningsManager;
    private final FollowManager followManager;

    private UserProfile currentUser;

    private MminoHubRepository() {
        musicCatalogue = new MusicCatalogue();
        releaseManager = new ReleaseManager();
        albumManager = new AlbumManager();
        libraryManager = new LibraryManager();
        purchaseManager = new PurchaseManager();
        notificationManager = new NotificationManager();
        earningsManager = new ArtistEarningsManager();
        followManager = new FollowManager();
    }

    public static synchronized MminoHubRepository getInstance() {
        if (instance == null) {
            instance = new MminoHubRepository();
        }

        return instance;
    }

    public MusicCatalogue getMusicCatalogue() {
        return musicCatalogue;
    }

    public ReleaseManager getReleaseManager() {
        return releaseManager;
    }

    public AlbumManager getAlbumManager() {
        return albumManager;
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    public PurchaseManager getPurchaseManager() {
        return purchaseManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public ArtistEarningsManager getEarningsManager() {
        return earningsManager;
    }

    public FollowManager getFollowManager() {
        return followManager;
    }

    public UserProfile getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserProfile currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isUserLoggedIn() {
        return currentUser != null && currentUser.isActive();
    }

    public void clearCurrentUser() {
        currentUser = null;
    }
}