package P3.Backend.DTO;

public class ContainerClass {
    
    // All of the variables that we want to store from each container.
    // That is also sent to the database.
    
    // Data from JSON:
    private String containerName;           // Is the name of the docker container.
    private String containerId;             // Is the id of the docker container.
    private Integer publicPort;             // Is the port number of the docker container.    
    private Integer containerInterval;      // Is the interval for sending data to another server. 

    // Data from docker:
    private Boolean containerRunning;       // Is docker container on or off.
    private Long containerRamUsage;         // Is ram usage on docker container.
    private Long containerCpuUsage;         // Is cpu usage on docker container.
    private Long containerDiskUsage;        // Is disk usage on docker container.
    private String containerStatus;         // Is the status of the docker container.
    private Long containerPid;              // Is the process id of the docker container / maybe? JVM main thread 🙏🙏🙏🙏.
    private Long containerExitCode;         // Is the exit code received from the program (CHROOT EXIT CODES - https://stackoverflow.com/questions/31297616/what-is-the-authoritative-list-of-docker-run-exit-codes).
    
    // Data from actuator:
    //JVM Data
    private Boolean JVMRunning;             // Is internally java application on or off.
    private Long JVMRamMax;               // Is max ram usage on java application.
    private Long JVMRamUsage;             // Is ram usage on java application.
    private Long JVMCpuUsagePerc;         // Is cpu usage on java application in percentage.
    private Integer JVMThreads;             // Is the number of threads on the java application.
    private Integer JVMThreadsStates;       // Is the number of threads states on the java application.
    private Integer JVMThreadQueued;       // Is the number of threads queued on the java application.
    private Long JVMCpuUsageStart;         // Is the start time of CPU usage on the java application.
    private Long JVMUptime;                // Is the uptime of the java application.
    
    //System Data
    private Long systemRamUsage;          // Is all of the ram usage on the system.
    private Long systemCpuUsagePerc;      // Is all of the cpu usage on the system in percentage.
    private Integer systemCpuCores;         // Is the number of cpu cores on the system.
    private Long systemDiskUsage;         // Is all of the disk usage on the system.
    private Long systemDiskTotal;         // Is the total disk space on the system.
    private Long systemDiskFree;          // Is the free disk space on the system.

    // Misc Data
    private Long timestamp;                 // Is the time when data is fetched.
    private Integer poolCore;             // Is the core number of threads allocated for the pool of the java application.
    private Integer logbackEvents;        // Is the number of logback events on the java application.
    private Integer logbackEventsError;   // Is the number of logback error events on the java application.
    private Integer logbackEventsWarn;    // Is the number of logback warn events on the java application.  
    private Long garbageCollectSize;   // Is the size of garbage collection on the java application. 
    
    //String JVMStatus;               // Is the status of the java application.
    
    ////////////////////////////////
    //  JSON GETTERS AND SETTERS  //
    ////////////////////////////////
    
    //ContainerName
    public String getContainerName() { return containerName; }
    
    public void setContainerName(String name) { this.containerName = name; }
    
    //ContainerId
    public String getContainerId() { return containerId; }
    
    public void setContainerId(String id) { this.containerId = id; }
    
    //PublicPort
    public Integer getPublicPort() { return publicPort; }
    
    public void setPublicPort(Integer publicPort) { this.publicPort = publicPort; }
    
    //ContainerInterval
    public Integer getContainerInterval() { return containerInterval; }
    
    public void setContainerInterval(Integer interval) { this.containerInterval = interval; }
    
    //////////////////////////////////////
    //  CONTAINER GETTERS AND SETTERS   //
    //////////////////////////////////////
    
    //ContainerRunning
    public Boolean getContainerRunning() { return containerRunning; }
    
    public void setContainerRunning(Boolean containerRunning) { this.containerRunning = containerRunning; }
    
    //ContainerRamUsage
    public Long getContainerRamUsage() { return containerRamUsage; }
    
    public void setContainerRamUsage(Long containerRamUsage) { this.containerRamUsage = containerRamUsage; }
    
    //ContainerCpuUsage
    public Long getContainerCpuUsage() { return containerCpuUsage; }
    
    public void setContainerCpuUsage(Long containerCpuUsage) { this.containerCpuUsage = containerCpuUsage; }
    
    //ContainerDiskUsage
    public Long getContainerDiskUsage() { return containerDiskUsage; }
    
    public void setContainerDiskUsage(Long containerDiskUsage) { this.containerDiskUsage = containerDiskUsage; }
    
    //ContainerStatus
    public String getContainerStatus() { return containerStatus; }
    
    public void setContainerStatus(String containerStatus) { this.containerStatus = containerStatus; }
    
    //ContainerPid
    public Long getContainerPid() { return containerPid; }
    
    public void setContainerPid(Long containerPid) { this.containerPid = containerPid; }

    //ExitCode    
    public Long getContainerExitCode() { return containerExitCode; }

    public void setContainerExitCode(Long containerExitCode) { this.containerExitCode = containerExitCode; }

    //////////////////////////////////////
    //   ACTUATOR GETTERS AND SETTERS   //
    //////////////////////////////////////

    //=================================//
    // JVM GETTERS AND SETTERS         //
    //=================================//

    //JVMRamMax
    public Long getJVMRamMax() { return JVMRamMax; }

    public void setJVMRamMax(Long jVMRamMax) { JVMRamMax = jVMRamMax; }

    //JVMRamUsage
    public Long getJVMRamUsage() { return JVMRamUsage; }

    public void setJVMRamUsage(Long jVMRamUsage) { JVMRamUsage = jVMRamUsage; }

    //JVMCpuUsagePerc
    public Long getJVMCpuUsagePerc() { return JVMCpuUsagePerc; }

    public void setJVMCpuUsagePerc(Long jVMCpuUsagePerc) { JVMCpuUsagePerc = jVMCpuUsagePerc; }

    //JVMThreads
    public Integer getJVMThreads() { return JVMThreads; }

    public void setJVMThreads(Integer jVMThreads) { JVMThreads = jVMThreads; }

    //JVMRunning
    public Boolean getJVMRunning() { return JVMRunning; }

    public void setJVMRunning(Boolean JVMRunning) { this.JVMRunning = JVMRunning; }

    //JVMThreadsStates
    public Integer getJVMThreadsStates() { return JVMThreadsStates; }

    public void setJVMThreadsStates(Integer JVMThreadsStates) { this.JVMThreadsStates = JVMThreadsStates; }

    //JVMThreadQueued
    public Integer getJVMThreadQueued() { return JVMThreadQueued; }

    public void setJVMThreadQueued(Integer jVMThreadQueued) { JVMThreadQueued = jVMThreadQueued; }

    //JVMCpuUsageStart
    public Long getJVMCpuUsageStart() { return JVMCpuUsageStart; }

    public void setJVMCpuUsageStart(Long jVMCpuUsageStart) { JVMCpuUsageStart = jVMCpuUsageStart; }

    //JVMUptime
    public Long getJVMUptime() { return JVMUptime; }

    public void setJVMUptime(Long jVMUptime) { JVMUptime = jVMUptime; }

    //=================================//
    //   SYSTEM GETTERS AND SETTERS    //
    //=================================//

//SystemRamUsage
    public Long getSystemRamUsage() { return systemRamUsage; }

    public void setSystemRamUsage(Long systemRamUsage) { this.systemRamUsage = systemRamUsage; }

//SystemCpuUsagePerc
    public Long getSystemCpuUsagePerc() { return systemCpuUsagePerc; }

    public void setSystemCpuUsagePerc(Long systemCpuUsagePerc) { this.systemCpuUsagePerc = systemCpuUsagePerc; }
    
//SystemCpuCores
    public Integer getSystemCpuCores() { return systemCpuCores; }

    public void setSystemCpuCores(Integer systemCpuCores) { systemCpuCores = systemCpuCores; }

//SystemDiskUsage
    public Long getSystemDiskUsage() { return systemDiskUsage; }

    public void setSystemDiskUsage(Long systemDiskUsage) { this.systemDiskUsage = systemDiskUsage; }

//SystemDiskTotal
    public Long getSystemDiskTotal() { return systemDiskTotal; }

    public void setSystemDiskTotal(Long systemDiskTotal) { this.systemDiskTotal = systemDiskTotal; }

//SystemDiskFree
    public Long getSystemDiskFree() { return systemDiskFree; }

    public void setSystemDiskFree(Long systemDiskFree) { this.systemDiskFree = systemDiskFree; }

    ////////////////////////////////
    //      MISC GET/SET HERE     //
    ////////////////////////////////

//PoolCore
    public Integer getPoolCore() { return poolCore; }

    public void setPoolCore(Integer poolCore) { this.poolCore = poolCore; }

//LogbackEvents
    public Integer getLogbackEvents() { return logbackEvents; }

    public void setLogbackEvents(Integer logbackEvents) { this.logbackEvents = logbackEvents; }

//LogbackEventsError
    public Integer getLogbackEventsError() { return logbackEventsError; }

    public void setLogbackEventsError(Integer logbackEventsError) { this.logbackEventsError = logbackEventsError; }

//LogbackEventsWarn
    public Integer getLogbackEventsWarn() { return logbackEventsWarn; }

    public void setLogbackEventsWarn(Integer logbackEventsWarn) { this.logbackEventsWarn = logbackEventsWarn; }

//GarbageCollectSize
    public Long getGarbageCollectSize() { return garbageCollectSize; }

    public void setGarbageCollectSize(Long garbageCollectSize) { this.garbageCollectSize = garbageCollectSize; }

//Timestamp
    public Long getTimestamp() { return timestamp; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

}
