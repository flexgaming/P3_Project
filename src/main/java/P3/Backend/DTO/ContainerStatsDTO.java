package P3.Backend.DTO;

public class ContainerStatsDTO {
    private Long timestamp;
    private String containerName;
    private String containerId;
    private Integer publicPort;
    private Integer containerInterval;
    private Boolean containerRunning;
    private Long containerRamUsage;
    private Long containerCpuUsage;
    private Long containerDiskUsage;
    private String containerStatus;
    private Integer containerPid;
    private Integer containerExitCode;
    private Long systemRamUsage;
    private Double systemCpuUsagePerc;
    private Long systemDiskUsage;
    private Long systemDiskTotal;
    private Long systemDiskFree;
    private Integer poolCore;
    private Integer logbackEvents;
    private Integer logbackEventsError;
    private Integer logbackEventsWarn;
    private Long garbageCollectSize;
    private Integer jvmthreads;
    private Integer jvmthreadsStates;
    private Long jvmcpuUsageStart;
    private Long jvmramMax;
    private Integer jvmthreadQueued;
    private Long jvmramUsage;
    private Double jvmcpuUsagePerc;
    private Integer systemCpuCores;
    private Long jvmuptime;
    private Boolean jvmrunning;
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getContainerName() {
        return containerName;
    }
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
    public String getContainerId() {
        return containerId;
    }
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
    public Integer getPublicPort() {
        return publicPort;
    }
    public void setPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
    }
    public Integer getContainerInterval() {
        return containerInterval;
    }
    public void setContainerInterval(Integer containerInterval) {
        this.containerInterval = containerInterval;
    }
    public Boolean getContainerRunning() {
        return containerRunning;
    }
    public void setContainerRunning(Boolean containerRunning) {
        this.containerRunning = containerRunning;
    }
    public Long getContainerRamUsage() {
        return containerRamUsage;
    }
    public void setContainerRamUsage(Long containerRamUsage) {
        this.containerRamUsage = containerRamUsage;
    }
    public Long getContainerCpuUsage() {
        return containerCpuUsage;
    }
    public void setContainerCpuUsage(Long containerCpuUsage) {
        this.containerCpuUsage = containerCpuUsage;
    }
    public Long getContainerDiskUsage() {
        return containerDiskUsage;
    }
    public void setContainerDiskUsage(Long containerDiskUsage) {
        this.containerDiskUsage = containerDiskUsage;
    }
    public String getContainerStatus() {
        return containerStatus;
    }
    public void setContainerStatus(String containerStatus) {
        this.containerStatus = containerStatus;
    }
    public Integer getContainerPid() {
        return containerPid;
    }
    public void setContainerPid(Integer containerPid) {
        this.containerPid = containerPid;
    }
    public Integer getContainerExitCode() {
        return containerExitCode;
    }
    public void setContainerExitCode(Integer containerExitCode) {
        this.containerExitCode = containerExitCode;
    }
    public Long getSystemRamUsage() {
        return systemRamUsage;
    }
    public void setSystemRamUsage(Long systemRamUsage) {
        this.systemRamUsage = systemRamUsage;
    }
    public Double getSystemCpuUsagePerc() {
        return systemCpuUsagePerc;
    }
    public void setSystemCpuUsagePerc(Double systemCpuUsagePerc) {
        this.systemCpuUsagePerc = systemCpuUsagePerc;
    }
    public Long getSystemDiskUsage() {
        return systemDiskUsage;
    }
    public void setSystemDiskUsage(Long systemDiskUsage) {
        this.systemDiskUsage = systemDiskUsage;
    }
    public Long getSystemDiskTotal() {
        return systemDiskTotal;
    }
    public void setSystemDiskTotal(Long systemDiskTotal) {
        this.systemDiskTotal = systemDiskTotal;
    }
    public Long getSystemDiskFree() {
        return systemDiskFree;
    }
    public void setSystemDiskFree(Long systemDiskFree) {
        this.systemDiskFree = systemDiskFree;
    }
    public Integer getPoolCore() {
        return poolCore;
    }
    public void setPoolCore(Integer poolCore) {
        this.poolCore = poolCore;
    }
    public Integer getLogbackEvents() {
        return logbackEvents;
    }
    public void setLogbackEvents(Integer logbackEvents) {
        this.logbackEvents = logbackEvents;
    }
    public Integer getLogbackEventsError() {
        return logbackEventsError;
    }
    public void setLogbackEventsError(Integer logbackEventsError) {
        this.logbackEventsError = logbackEventsError;
    }
    public Integer getLogbackEventsWarn() {
        return logbackEventsWarn;
    }
    public void setLogbackEventsWarn(Integer logbackEventsWarn) {
        this.logbackEventsWarn = logbackEventsWarn;
    }
    public Long getGarbageCollectSize() {
        return garbageCollectSize;
    }
    public void setGarbageCollectSize(Long garbageCollectSize) {
        this.garbageCollectSize = garbageCollectSize;
    }
    public Integer getJvmthreads() {
        return jvmthreads;
    }
    public void setJvmthreads(Integer jvmthreads) {
        this.jvmthreads = jvmthreads;
    }
    public Integer getJvmthreadsStates() {
        return jvmthreadsStates;
    }
    public void setJvmthreadsStates(Integer jvmthreadsStates) {
        this.jvmthreadsStates = jvmthreadsStates;
    }
    public Long getJvmcpuUsageStart() {
        return jvmcpuUsageStart;
    }
    public void setJvmcpuUsageStart(Long jvmcpuUsageStart) {
        this.jvmcpuUsageStart = jvmcpuUsageStart;
    }
    public Long getJvmramMax() {
        return jvmramMax;
    }
    public void setJvmramMax(Long jvmramMax) {
        this.jvmramMax = jvmramMax;
    }
    public Integer getJvmthreadQueued() {
        return jvmthreadQueued;
    }
    public void setJvmthreadQueued(Integer jvmthreadQueued) {
        this.jvmthreadQueued = jvmthreadQueued;
    }
    public Long getJvmramUsage() {
        return jvmramUsage;
    }
    public void setJvmramUsage(Long jvmramUsage) {
        this.jvmramUsage = jvmramUsage;
    }
    public Double getJvmcpuUsagePerc() {
        return jvmcpuUsagePerc;
    }
    public void setJvmcpuUsagePerc(Double jvmcpuUsagePerc) {
        this.jvmcpuUsagePerc = jvmcpuUsagePerc;
    }
    public Integer getSystemCpuCores() {
        return systemCpuCores;
    }
    public void setSystemCpuCores(Integer systemCpuCores) {
        this.systemCpuCores = systemCpuCores;
    }
    public Long getJvmuptime() {
        return jvmuptime;
    }
    public void setJvmuptime(Long jvmuptime) {
        this.jvmuptime = jvmuptime;
    }
    public Boolean getJvmrunning() {
        return jvmrunning;
    }
    public void setJvmrunning(Boolean jvmrunning) {
        this.jvmrunning = jvmrunning;
    }
    public ContainerStatsDTO(Long timestamp, String containerName, String containerId, Integer publicPort,
            Integer containerInterval, Boolean containerRunning, Long containerRamUsage, Long containerCpuUsage,
            Long containerDiskUsage, String containerStatus, Integer containerPid, Integer containerExitCode,
            Long systemRamUsage, Double systemCpuUsagePerc, Long systemDiskUsage, Long systemDiskTotal,
            Long systemDiskFree, Integer poolCore, Integer logbackEvents, Integer logbackEventsError,
            Integer logbackEventsWarn, Long garbageCollectSize, Integer jvmthreads, Integer jvmthreadsStates,
            Long jvmcpuUsageStart, Long jvmramMax, Integer jvmthreadQueued, Long jvmramUsage, Double jvmcpuUsagePerc,
            Integer systemCpuCores, Long jvmuptime, Boolean jvmrunning) {
        this.timestamp = timestamp;
        this.containerName = containerName;
        this.containerId = containerId;
        this.publicPort = publicPort;
        this.containerInterval = containerInterval;
        this.containerRunning = containerRunning;
        this.containerRamUsage = containerRamUsage;
        this.containerCpuUsage = containerCpuUsage;
        this.containerDiskUsage = containerDiskUsage;
        this.containerStatus = containerStatus;
        this.containerPid = containerPid;
        this.containerExitCode = containerExitCode;
        this.systemRamUsage = systemRamUsage;
        this.systemCpuUsagePerc = systemCpuUsagePerc;
        this.systemDiskUsage = systemDiskUsage;
        this.systemDiskTotal = systemDiskTotal;
        this.systemDiskFree = systemDiskFree;
        this.poolCore = poolCore;
        this.logbackEvents = logbackEvents;
        this.logbackEventsError = logbackEventsError;
        this.logbackEventsWarn = logbackEventsWarn;
        this.garbageCollectSize = garbageCollectSize;
        this.jvmthreads = jvmthreads;
        this.jvmthreadsStates = jvmthreadsStates;
        this.jvmcpuUsageStart = jvmcpuUsageStart;
        this.jvmramMax = jvmramMax;
        this.jvmthreadQueued = jvmthreadQueued;
        this.jvmramUsage = jvmramUsage;
        this.jvmcpuUsagePerc = jvmcpuUsagePerc;
        this.systemCpuCores = systemCpuCores;
        this.jvmuptime = jvmuptime;
        this.jvmrunning = jvmrunning;
    }
}
