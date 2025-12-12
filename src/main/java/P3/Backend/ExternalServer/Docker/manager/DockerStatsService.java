package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.stereotype.Service;

@Service
public class DockerStatsService {

    private final DockerClient dockerClient;

    public DockerStatsService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    public ContainerStats getContainerStats(String containerId) throws InterruptedException {
        SingleStatsCallback callback = new SingleStatsCallback();
        dockerClient.statsCmd(containerId).exec(callback).awaitCompletion();
        Statistics stats = callback.getStats();
        return stats != null ? new ContainerStats(stats) : null;
    }
}