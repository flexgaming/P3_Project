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
    public static final String CONTAINER_NAME = "currentContainers.json";
    public static final String COMPANY_INFO = "currentCompany.json";
    public static final String CURRENT_CONTAINER_PATH = ""; // Replace "" with desired path. 

    // -------------------------------
    // Application Settings
    // -------------------------------
    public static final Integer DEFAULT_INTERVAL_TIME = 60; // Default interval time in seconds.
    // -------------------------------
    // Example URLs or endpoints
    // -------------------------------
    public static final String WINDOWS_DOCKER_SOCKET = "npipe:////./pipe/docker_engine";
    public static final String LINUX_MAC_DOCKER_SOCKET = "unix:///var/run/docker.sock";

    public static final String SPRING_ACTUATOR_DEFAULT_ENDPOINT = "http://localhost";

    public static final String INTERNAL_SERVER_URL = "https://pulpiest-tad-unamalgamated.ngrok-free.dev/api/upload-json";
}