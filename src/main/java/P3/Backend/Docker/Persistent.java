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
    public static final Integer DEFAULT_INTERVAL_TIME = 60;
    // -------------------------------
    // Example URLs or endpoints
    // -------------------------------
    public static final String WINDOWS_DOCKER_SOCKET = "npipe:////./pipe/docker_engine";
    public static final String LINUX_MAC_DOCKER_SOCKET = "unix:///var/run/docker.sock";

    public static final String SPRING_ACTUATOR_DEFAULT_ENDPOINT = "http://localhost";
            
}