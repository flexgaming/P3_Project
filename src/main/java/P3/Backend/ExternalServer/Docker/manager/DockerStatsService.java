package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallbackTemplate;
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

    /** This function is used to fetch get the first snapshot of the incoming stream.
     *
     * Hereby, setting the stats to be the latest statistics from the container.
     */
    private static class SingleStatsCallback extends ResultCallbackTemplate<SingleStatsCallback, Statistics> {
        private Statistics stats;

        /** This function is used to as a sort of API to get the latest stream of data.
         *
         * It will stop when it has got the latest snapshot of data.
         *
         * @param statistics Is used to set all of the stats that the docker container has.
         */
        @Override
        public void onNext(Statistics statistics) {
            this.stats = statistics;
            try {
                this.close(); // stop after first snapshot
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /** This function is used to retrieve the latest result from the onNext function.
         *
         * @return Is a statistics object with all of the container stats data.
         */
        public Statistics getStats() {
            return stats;
        }
    }

    /** This class is made to store all of the data within a single class. */
    public static class ContainerStats {
        private final Double cpuTotalUsage;
        private final Double systemCpuTotalUsage;
        private final int cpuCount;
        private final Long memoryUsage;
        private final Long memoryLimit;
        private final int pids;

        // Constructor for setting all of the stats data into the class' parameters.
        public ContainerStats(Statistics stats) {
            this.cpuTotalUsage = stats.getCpuStats().getCpuUsage().getTotalUsage().doubleValue();
            this.systemCpuTotalUsage = stats.getCpuStats().getSystemCpuUsage().doubleValue();
            // Docker can return cpu count in two different ways.
            this.cpuCount = stats.getCpuStats().getOnlineCpus() != null
                    ? stats.getCpuStats().getOnlineCpus().intValue()
                    : stats.getCpuStats().getCpuUsage().getPercpuUsage().size();
            this.memoryUsage = stats.getMemoryStats().getUsage();
            this.memoryLimit = stats.getMemoryStats().getLimit();
            this.pids = stats.getPidsStats().getCurrent().intValue();
        }

        // Getters
        public Double getCpuTotalUsage() { return cpuTotalUsage; }
        public Double getSystemCpuTotalUsage() { return systemCpuTotalUsage; }
        public Long getMemoryUsage() { return memoryUsage; }
        public Long getMemoryLimit() { return memoryLimit; }
        public int getPids() { return pids; }
        public int getCpuCount() { return cpuCount; }
    }
}