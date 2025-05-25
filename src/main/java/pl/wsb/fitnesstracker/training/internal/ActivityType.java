package pl.wsb.fitnesstracker.training.internal;

/**
 * Enum representing different types of training activities.
 */
public enum ActivityType {

    /**
     * Running activity.
     */
    RUNNING("Running"),
    /**
     * Cycling activity.
     */
    CYCLING("Cycling"),
    /**
     * Walking activity.
     */
    WALKING("Walking"),
    /**
     * Swimming activity.
     */
    SWIMMING("Swimming"),
    /**
     * Tennis activity.
     */
    TENNIS("Tennis");

    private final String displayName;

    /**
     * Constructs an ActivityType with a display name.
     *
     * @param displayName the display name of the activity
     */
    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the activity.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }
}