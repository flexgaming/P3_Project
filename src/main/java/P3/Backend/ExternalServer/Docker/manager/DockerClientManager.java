package P3.Backend.ExternalServer.Docker.manager;

import java.util.List;

import org.json.JSONArray;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
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

    
    public static JSONArray ListAllContainers(DockerClient dockerClient) {
        List<Container> containersTemp = dockerClient.listContainersCmd().withShowAll(true).exec();
        JSONArray containers = new JSONArray(containersTemp);
        return containers;
    }
}