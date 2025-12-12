package P3.Backend.ExternalServer.Docker.manager;

import com.github.dockerjava.api.model.Statistics;

/** This class is made to store all of the data within a single class. */
public class ContainerStats {
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