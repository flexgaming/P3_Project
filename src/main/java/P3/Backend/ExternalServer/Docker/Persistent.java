package P3.Backend.ExternalServer.Docker;


public final class Persistent {

    
    private Persistent() {}

    
    
    
    public static final String CONTAINER_NAME = "currentContainers.json";
    public static final String SERVER_INFO = "serverInfo.json";
    public static final String CURRENT_CONTAINER_PATH = ""; 

    
    
    
    public static final Integer DEFAULT_INTERVAL_TIME = 60; 
    public static final Integer DEFAULT_HEARTBEAT_TIME = 5; 

    
    
    
    public static final String WINDOWS_DOCKER_SOCKET = "npipe:////./pipe/docker_engine";
    public static final String LINUX_MAC_DOCKER_SOCKET = "unix:///var/run/docker.sock";

    public static final String SPRING_ACTUATOR_DEFAULT_ENDPOINT = "http://localhost";

    
    public static final String BACKEND_SERVER_URL = "http://localhost:8080/api";
}