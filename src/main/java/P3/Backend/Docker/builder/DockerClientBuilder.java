package P3.Backend.Docker.builder;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// import P3.Backend.Docker.manager.DockerClientManager;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;

import P3.Backend.Docker.manager.DockerClientManager;

@Configuration
public class DockerClientBuilder {
    @Bean
    public DockerClient dockerClient () {
        // Detect platform and select appropriate socket/pipe
        String dockerHost;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            dockerHost = "npipe:////./pipe/docker_engine";  // Windows named pipe
        } else {
            dockerHost = "unix:///var/run/docker.sock";     // Linux/macOS socket
        }

        DockerClientConfig config = DockerClientManager.initializeDockerClient(dockerHost);

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
            .dockerHost(config.getDockerHost())
            .sslConfig(config.getSSLConfig())
            .maxConnections(100)
            .connectionTimeout(Duration.ofSeconds(30))
            .responseTimeout(Duration.ofSeconds(45))
            .build();
            
        return DockerClientImpl.getInstance(config, httpClient);
    }
}

