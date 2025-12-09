package P3.Backend.ExternalServer.Docker;

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
    public static final Integer DEFAULT_HEARTBEAT_TIME = 5; // Default interval time in seconds.

    // -------------------------------
    // Example URLs or endpoints
    // -------------------------------
    public static final String WINDOWS_DOCKER_SOCKET = "npipe:////./pipe/docker_engine";
    public static final String LINUX_MAC_DOCKER_SOCKET = "unix:///var/run/docker.sock";

    public static final String SPRING_ACTUATOR_DEFAULT_ENDPOINT = "http://localhost";

    // api is the endpoint that is being listening on, on the backend server.
    public static final String BACKEND_SERVER_URL = "https://pulpiest-tad-unamalgamated.ngrok-free.dev/api";
}