package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.model.Statistics;
import org.springframework.stereotype.Service;

@Service
public class DockerStatsService {

    private final DockerClient dockerClient;

    /**
     * Use your existing DockerClient instance
     */
    public DockerStatsService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Get a snapshot of container stats as a POJO
     */
    public ContainerStats getContainerStats(String containerId) throws InterruptedException {
        SingleStatsCallback callback = new SingleStatsCallback();
        dockerClient.statsCmd(containerId).exec(callback).awaitCompletion();
        Statistics stats = callback.getStats();
        return stats != null ? new ContainerStats(stats) : null;
    }

    /**
     * Internal callback to fetch only the first snapshot
     */
    private static class SingleStatsCallback extends ResultCallbackTemplate<SingleStatsCallback, Statistics> {
        private Statistics stats;

        @Override
        public void onNext(Statistics statistics) {
            this.stats = statistics;
            try {
                this.close(); // stop after first snapshot
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        public Statistics getStats() {
            return stats;
        }
    }

    /**
     * Simple POJO to hold stats
     */
    public static class ContainerStats {
        private final long cpuTotalUsage;
        private final long memoryUsage;
        private final long memoryLimit;
        private final int pids;

        public ContainerStats(Statistics stats) {
            this.cpuTotalUsage = stats.getCpuStats().getCpuUsage().getTotalUsage();
            this.memoryUsage = stats.getMemoryStats().getUsage();
            this.memoryLimit = stats.getMemoryStats().getLimit();
            this.pids = stats.getPidsStats().getCurrent().intValue();
        }

        // Getters
        public long getCpuTotalUsage() { return cpuTotalUsage; }
        public long getMemoryUsage() { return memoryUsage; }
        public long getMemoryLimit() { return memoryLimit; }
        public int getPids() { return pids; }
    }
}