package P3.Backend.Docker;

/**
 * Persistent constants for file paths and application-wide settings.
 * Can be imported anywhere in the project.
 */
public final class Persistent {

    // Prevent instantiation
    private Persistent() {}

    // -------------------------------
    // File Paths
    // -------------------------------
    public static final String CURRENT_CONTAINER_PATH = "currentContainers.json";
    // public static final String BACKUP_PATH = "/data/backups/";
    // public static final String TEMP_PATH = "/tmp/myapp/";
    // public static final String LOGS_PATH = "/var/log/myapp/";

    // -------------------------------
    // Application Settings
    // -------------------------------

    // -------------------------------
    // Example URLs or endpoints
    // -------------------------------
    // public static final String API_BASE_URL = "https://api.example.com/";
    // public static final String HEALTH_ENDPOINT = API_BASE_URL + "health";

}