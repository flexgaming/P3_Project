package P3.Backend.DTO;

public class ContainerStatsDTO {
    private Long timestamp;
    
    // Data from JSON:
    private String containerName;
    private String containerId;
    private Integer publicPort;
    private Integer containerInterval;

    // Data from docker:
    private Boolean containerRunning;
    private Long containerRamUsage;
    private Long containerCpuUsage;
    private Long containerDiskUsage;
    private String containerStatus;
    private Long containerPid;
    private Long containerExitCode;
    

    // Data from actuator:
    //JVM Data
    private Boolean JVMRunning;
    private Long JVMRamMax;
    private Long JVMRamUsage;
    private Long JVMCpuUsagePerc;
    private Integer JVMThreads;
    private Integer JVMThreadsStates;
    private Integer JVMThreadQueued;
    private Long JVMCpuUsageStart;
    private Long JVMUptime;
    
    //System Data
    private Long systemRamUsage;
    private Long systemCpuUsagePerc;
    private Integer SystemCpuCores;
    private Long systemDiskUsage;
    private Long systemDiskTotal;
    private Long systemDiskFree;

    // Misc Data
    private Integer poolCore; 
    private Integer logbackEvents;
    private Integer logbackEventsError;
    private Integer logbackEventsWarn; 
    private Long garbageCollectSize; 

    // ----- Getters & Setters -----

    public Long getTimestamp() { return timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    public String getContainerName() { return containerName; }
    public void setContainerName(String containerName) { this.containerName = containerName; }

    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }

    public Integer getPublicPort() { return publicPort; }
    public void setPublicPort(Integer publicPort) { this.publicPort = publicPort; }

    public Integer getContainerInterval() { return containerInterval; }
    public void setContainerInterval(Integer containerInterval) { this.containerInterval = containerInterval; }

    public Boolean getContainerRunning() { return containerRunning; }
    public void setContainerRunning(Boolean containerRunning) { this.containerRunning = containerRunning; }

    public Long getContainerRamUsage() { return containerRamUsage; }
    public void setContainerRamUsage(Long containerRamUsage) { this.containerRamUsage = containerRamUsage; }

    public Long getContainerCpuUsage() { return containerCpuUsage; }
    public void setContainerCpuUsage(Long containerCpuUsage) { this.containerCpuUsage = containerCpuUsage; }

    public Long getContainerDiskUsage() { return containerDiskUsage; }
    public void setContainerDiskUsage(Long containerDiskUsage) { this.containerDiskUsage = containerDiskUsage; }

    public String getContainerStatus() { return containerStatus; }
    public void setContainerStatus(String containerStatus) { this.containerStatus = containerStatus; }

    public Long getContainerPid() { return containerPid; }
    public void setContainerPid(Long containerPid) { this.containerPid = containerPid; }

    public Long getContainerExitCode() { return containerExitCode; }
    public void setContainerExitCode(Long containerExitCode) { this.containerExitCode = containerExitCode; }

    public Boolean getJVMRunning() { return JVMRunning; }
    public void setJVMRunning(Boolean JVMRunning) { this.JVMRunning = JVMRunning; }

    public Long getJVMRamMax() { return JVMRamMax; }
    public void setJVMRamMax(Long JVMRamMax) { this.JVMRamMax = JVMRamMax; }

    public Long getJVMRamUsage() { return JVMRamUsage; }
    public void setJVMRamUsage(Long JVMRamUsage) { this.JVMRamUsage = JVMRamUsage; }

    public Long getJVMCpuUsagePerc() { return JVMCpuUsagePerc; }
    public void setJVMCpuUsagePerc(Long JVMCpuUsagePerc) { this.JVMCpuUsagePerc = JVMCpuUsagePerc; }

    public Integer getJVMThreads() { return JVMThreads; }
    public void setJVMThreads(Integer JVMThreads) { this.JVMThreads = JVMThreads; }

    public Integer getJVMThreadsStates() { return JVMThreadsStates; }
    public void setJVMThreadsStates(Integer JVMThreadsStates) { this.JVMThreadsStates = JVMThreadsStates; }

    public Integer getJVMThreadQueued() { return JVMThreadQueued; }
    public void setJVMThreadQueued(Integer JVMThreadQueued) { this.JVMThreadQueued = JVMThreadQueued; }

    public Long getJVMCpuUsageStart() { return JVMCpuUsageStart; }
    public void setJVMCpuUsageStart(Long JVMCpuUsageStart) { this.JVMCpuUsageStart = JVMCpuUsageStart; }

    public Long getJVMUptime() { return JVMUptime; }
    public void setJVMUptime(Long JVMUptime) { this.JVMUptime = JVMUptime; }

    public Long getSystemRamUsage() { return systemRamUsage; }
    public void setSystemRamUsage(Long systemRamUsage) { this.systemRamUsage = systemRamUsage; }

    public Long getSystemCpuUsagePerc() { return systemCpuUsagePerc; }
    public void setSystemCpuUsagePerc(Long systemCpuUsagePerc) { this.systemCpuUsagePerc = systemCpuUsagePerc; }

    public Integer getSystemCpuCores() { return SystemCpuCores; }
    public void setSystemCpuCores(Integer systemCpuCores) { this.SystemCpuCores = systemCpuCores; }

    public Long getSystemDiskUsage() { return systemDiskUsage; }
    public void setSystemDiskUsage(Long systemDiskUsage) { this.systemDiskUsage = systemDiskUsage; }

    public Long getSystemDiskTotal() { return systemDiskTotal; }
    public void setSystemDiskTotal(Long systemDiskTotal) { this.systemDiskTotal = systemDiskTotal; }

    public Long getSystemDiskFree() { return systemDiskFree; }
    public void setSystemDiskFree(Long systemDiskFree) { this.systemDiskFree = systemDiskFree; }

    public Integer getPoolCore() { return poolCore; }
    public void setPoolCore(Integer poolCore) { this.poolCore = poolCore; }

    public Integer getLogbackEvents() { return logbackEvents; }
    public void setLogbackEvents(Integer logbackEvents) { this.logbackEvents = logbackEvents; }

    public Integer getLogbackEventsError() { return logbackEventsError; }
    public void setLogbackEventsError(Integer logbackEventsError) { this.logbackEventsError = logbackEventsError; }

    public Integer getLogbackEventsWarn() { return logbackEventsWarn; }
    public void setLogbackEventsWarn(Integer logbackEventsWarn) { this.logbackEventsWarn = logbackEventsWarn; }

    public Long getGarbageCollectSize() { return garbageCollectSize; }
    public void setGarbageCollectSize(Long garbageCollectSize) { this.garbageCollectSize = garbageCollectSize; }

    // ----- Constructor -----

    public ContainerStatsDTO(
            Long timestamp, String containerName, String containerId, Integer publicPort,
            Integer containerInterval, Boolean containerRunning, Long containerRamUsage,
            Long containerCpuUsage, Long containerDiskUsage, String containerStatus,
            Long containerPid, Long containerExitCode, Boolean JVMRunning, Long JVMRamMax,
            Long JVMRamUsage, Long JVMCpuUsagePerc, Integer JVMThreads, Integer JVMThreadsStates,
            Integer JVMThreadQueued, Long JVMCpuUsageStart, Long JVMUptime,
            Long systemRamUsage, Long systemCpuUsagePerc, Integer SystemCpuCores,
            Long systemDiskUsage, Long systemDiskTotal, Long systemDiskFree,
            Integer poolCore, Integer logbackEvents, Integer logbackEventsError,
            Integer logbackEventsWarn, Long garbageCollectSize
    ) {
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

        this.JVMRunning = JVMRunning;
        this.JVMRamMax = JVMRamMax;
        this.JVMRamUsage = JVMRamUsage;
        this.JVMCpuUsagePerc = JVMCpuUsagePerc;
        this.JVMThreads = JVMThreads;
        this.JVMThreadsStates = JVMThreadsStates;
        this.JVMThreadQueued = JVMThreadQueued;
        this.JVMCpuUsageStart = JVMCpuUsageStart;
        this.JVMUptime = JVMUptime;

        this.systemRamUsage = systemRamUsage;
        this.systemCpuUsagePerc = systemCpuUsagePerc;
        this.SystemCpuCores = SystemCpuCores;
        this.systemDiskUsage = systemDiskUsage;
        this.systemDiskTotal = systemDiskTotal;
        this.systemDiskFree = systemDiskFree;

        this.poolCore = poolCore;
        this.logbackEvents = logbackEvents;
        this.logbackEventsError = logbackEventsError;
        this.logbackEventsWarn = logbackEventsWarn;
        this.garbageCollectSize = garbageCollectSize;
    }
}
