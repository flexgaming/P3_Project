package P3.Backend.Docker.manager;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;


public class DockerClientManager {  
    public static final DockerClientConfig initializeDockerClient(String dockerHost) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withDockerHost(dockerHost)
            .build();
        System.out.println("Docker Client Config created successfully");
        return config;
    }
}
