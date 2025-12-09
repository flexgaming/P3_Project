package P3.Backend.ExternalServer.Docker.manager;

import java.util.List;

import org.json.JSONArray;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig;

public class DockerClientManager {

    /** This function is used to make the config.
     *
     * @param dockerHost Is used to figure out what OS the host is using.
     * @return Is the config.
     */
    public static final DockerClientConfig initializeDockerClient(String dockerHost) {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(dockerHost)
                .build();
        System.out.println("Docker Client Config created successfully");
        return config;
    }

    /** This function is used to list all of the containers on the Docker backend.
     *
     * @param dockerClient Is used to establish a connection between Docker backend and the application.
     * @return Is a JSONArray containg all of the containers and their data.
     */
    public static JSONArray ListAllContainers(DockerClient dockerClient) {
        List<Container> containersTemp = dockerClient.listContainersCmd().withShowAll(true).exec();
        JSONArray containers = new JSONArray(containersTemp);
        return containers;
    }
}