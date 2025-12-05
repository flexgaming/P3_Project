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
        private final Double cpuTotalUsage;
        private final Double systemCpuTotalUsage;
        private final int cpuCount;
        private final Double memoryUsage;
        private final Double memoryLimit;
        private final int pids;

        public ContainerStats(Statistics stats) {
            this.cpuTotalUsage = stats.getCpuStats().getCpuUsage().getTotalUsage().doubleValue();
            this.systemCpuTotalUsage = stats.getCpuStats().getSystemCpuUsage().doubleValue();
            // Docker can return cpu count in two different ways.
            this.cpuCount = stats.getCpuStats().getOnlineCpus() != null
                    ? stats.getCpuStats().getOnlineCpus().intValue()
                    : stats.getCpuStats().getCpuUsage().getPercpuUsage().size();
            this.memoryUsage = stats.getMemoryStats().getUsage().doubleValue();
            this.memoryLimit = stats.getMemoryStats().getLimit().doubleValue();
            this.pids = stats.getPidsStats().getCurrent().intValue();
        }

        // Getters
        public Double getCpuTotalUsage() { return cpuTotalUsage; }
        public Double getSystemCpuTotalUsage() { return systemCpuTotalUsage; }
        public Double getMemoryUsage() { return memoryUsage; }
        public Double getMemoryLimit() { return memoryLimit; }
        public int getPids() { return pids; }
        public int getCpuCount() { return cpuCount; }
    }
}