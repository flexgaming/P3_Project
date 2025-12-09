package P3.Backend.Docker.builder;

import static P3.Backend.Docker.Persistent.WINDOWS_DOCKER_SOCKET;
import static P3.Backend.Docker.Persistent.LINUX_MAC_DOCKER_SOCKET;
import P3.Backend.Docker.manager.DockerClientManager;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

// Creates a Docker Client bean to connect to the Docker environment
@Configuration
public class DockerClientBuilder {
    /**
     * Creates and configures a Docker client bean for connecting to Docker
     * @return configured DockerClient instance
     */
    @Bean
    public DockerClient dockerClient () {
        String dockerHost;
        // Check OS and set the correct Docker socket/pipe path
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            dockerHost = WINDOWS_DOCKER_SOCKET;     // Windows named pipe
        } else {
            dockerHost = LINUX_MAC_DOCKER_SOCKET;   // Linux/macOS socket
        }

        // Initialize Docker client configuration with detected host
        DockerClientConfig config = DockerClientManager.initializeDockerClient(dockerHost);

        // Build HTTP client for Docker API communication
        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
            .dockerHost(config.getDockerHost())
            .sslConfig(config.getSSLConfig())
            .maxConnections(100)
            .connectionTimeout(Duration.ofSeconds(30))
            .responseTimeout(Duration.ofSeconds(45))
            .build();
            
        // Create and return Docker client instance
        return DockerClientImpl.getInstance(config, httpClient);
    }


    /**
     * Builds the configuration and connection to the Docker Deamon.
     * @return configured DockerClient instance from dockerClient()
     */
    @Bean
    public static DockerClient dockerConnection() {

        // Create builder instance and get configured Docker client
        DockerClientBuilder builder = new DockerClientBuilder();
		DockerClient dockerClient = builder.dockerClient();

        return dockerClient;
    }
}

