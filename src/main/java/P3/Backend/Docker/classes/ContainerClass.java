package P3.Backend.Docker.classes;

import java.util.Date;

public class ContainerClass {
    
    // All of the variables that we want to store from each container.
    // That is also sent to the database.
    
    Long timestamp;                 // Is the time when data is fetched.
    
    // Data from JSON:
    String containerName;           // Is the name of the docker container.
    String containerId;             // Is the id of the docker container.
    Integer publicPort;             // Is the port number of the docker container.    
    Integer containerInterval;      // Is the interval for sending data to another server. 

    // Data from docker:
    Boolean containerRunning;       // Is docker container on or off.
    Long containerRamUsage;         // Is ram usage on docker container.
    Long containerCpuUsage;         // Is cpu usage on docker container.
    Long containerDiskUsage;        // Is disk usage on docker container.
    String containerStatus;         // Is the status of the docker container.
    Long containerPid;              // Is the process id of the docker container / maybe? JVM main thread 🙏🙏🙏🙏.
    Long containerExitCode;         // Is the exit code received from the program (CHROOT EXIT CODES - https://stackoverflow.com/questions/31297616/what-is-the-authoritative-list-of-docker-run-exit-codes).
    

    // Data from actuator:
    //JVM Data
    Boolean JVMRunning;             // Is internally java application on or off.
    long JVMRamMax;               // Is max ram usage on java application.
    Long JVMRamUsage;             // Is ram usage on java application.
    Long JVMCpuUsagePerc;         // Is cpu usage on java application in percentage.
    Integer JVMThreads;             // Is the number of threads on the java application.
    Integer JVMThreadsStates;       // Is the number of threads states on the java application.
    Integer JVMThreadQueued;       // Is the number of threads queued on the java application.
    Long JVMCpuUsageStart;         // Is the start time of CPU usage on the java application.
    Long JVMUptime;                // Is the uptime of the java application.
    
    //System Data
    Long systemRamUsage;          // Is all of the ram usage on the system.
    Long systemCpuUsagePerc;      // Is all of the cpu usage on the system in percentage.
    Integer SystemCpuCores;         // Is the number of cpu cores on the system.
    Long systemDiskUsage;         // Is all of the disk usage on the system.
    Long systemDiskTotal;         // Is the total disk space on the system.
    Long systemDiskFree;          // Is the free disk space on the system.

    // Misc Data
    Integer poolCore;             // Is the core number of threads allocated for the pool of the java application.
    Integer logbackEvents;        // Is the number of logback events on the java application.
    Integer logbackEventsError;   // Is the number of logback error events on the java application.
    Integer logbackEventsWarn;    // Is the number of logback warn events on the java application.  
    Long garbageCollectSize;   // Is the size of garbage collection on the java application.
    
    
    //String JVMStatus;               // Is the status of the java application.
    
    
    
    
    
    
    
    /////////////////////////
    //      ERROR HERE     //
    /////////////////////////
    
    public ContainerClass(String name, String id, Integer interval, Integer publicPort) {
        this.containerName = name;
        this.containerId = id;
        this.containerInterval = interval;
        this.publicPort = publicPort;
    }
    
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
    //  ACTUATOR GETTERS AND SETTERS    //
    //////////////////////////////////////

    //=================================//
    // JVM GETTERS AND SETTERS         //
    //=================================//

    //JVMRamMax
    public long getJVMRamMax() { return JVMRamMax; }

    public void setJVMRamMax(long jVMRamMax) { JVMRamMax = jVMRamMax; }

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
    public Integer getSystemCpuCores() { return SystemCpuCores; }

    public void setSystemCpuCores(Integer systemCpuCores) { SystemCpuCores = systemCpuCores; }

//SystemDiskUsage
    public Long getSystemDiskUsage() { return systemDiskUsage; }

    public void setSystemDiskUsage(Long systemDiskUsage) { this.systemDiskUsage = systemDiskUsage; }

//SystemDiskTotal
    public Long getSystemDiskTotal() { return systemDiskTotal; }

    public void setSystemDiskTotal(Long systemDiskTotal) { this.systemDiskTotal = systemDiskTotal; }

//SystemDiskFree
    public Long getSystemDiskFree() { return systemDiskFree; }

    public void setSystemDiskFree(Long systemDiskFree) { this.systemDiskFree = systemDiskFree; }

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










    
    /////////////////////////////////
    //      ERROR GET/SET HERE     //
    /////////////////////////////////


}
