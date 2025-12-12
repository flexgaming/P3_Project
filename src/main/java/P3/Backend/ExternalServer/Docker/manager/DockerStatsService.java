package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.stereotype.Service;

@Service
public class DockerStatsService {

    private final DockerClient dockerClient;

    /** This function is used to make a connection between the Docker backend and the application
     *
     * @param dockerClient Is used to establish a connection to the existing DockerClient instance.
     */
    public DockerStatsService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /** This function is used to read container statistics.
     *
     * @param containerId Is used in order to get the right container.
     * @return Is a stats object with how much hardware the container is using (CPU, RAM, DISK, THREAD, NETWORK).
     * @throws InterruptedException Is used if the stats retrival is interrupted.
     */
    public ContainerStats getContainerStats(String containerId) throws InterruptedException {
        SingleStatsCallback callback = new SingleStatsCallback();
        dockerClient.statsCmd(containerId).exec(callback).awaitCompletion();
        Statistics stats = callback.getStats();
        return stats != null ? new ContainerStats(stats) : null;
    }
}