package P3.Backend.ExternalServer.Docker.builder;

import static P3.Backend.ExternalServer.Docker.Persistent.WINDOWS_DOCKER_SOCKET;
import static P3.Backend.ExternalServer.Docker.Persistent.LINUX_MAC_DOCKER_SOCKET;
import P3.Backend.ExternalServer.Docker.manager.DockerClientManager;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;


@Configuration
public class DockerClientBuilder {
    @Bean
    public DockerClient dockerClient () {
        
        String dockerHost;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            dockerHost = WINDOWS_DOCKER_SOCKET;     
        } else {
            dockerHost = LINUX_MAC_DOCKER_SOCKET;   
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

    @Bean
    public static DockerClient dockerConnection() {

        DockerClientBuilder builder = new DockerClientBuilder();
		DockerClient dockerClient = builder.dockerClient();

        return dockerClient;
    }
}

