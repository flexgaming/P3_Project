package P3.Backend.Docker.classes;

import java.util.Date;

public class ContainerClass {
    
    // All of the variables that we want to store from each container.
    // That is also sent to the database.
    
    Date timestamp;                 // Is the time when data is fetched.
    
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

    
    // Data from actuator:
    Boolean JVMRunning;             // Is internally java application on or off.
    Long JVMRamUsage;             // Is ram usage on java application.
    Long JVMCpuUsagePerc;         // Is cpu usage on java application in percentage.
    Long systemRamUsage;          // Is all of the ram usage on the system.
    Long systemCpuUsagePerc;      // Is all of the cpu usage on the system in percentage.
    Integer SystemCpuCores;         // Is the number of cpu cores on the system.
    Long systemDiskUsage;         // Is all of the disk usage on the system.
    Long systemDiskTotal;         // Is the total disk space on the system.
    Long systemDiskFree;          // Is the free disk space on the system.
    Integer JVMThreads;             // Is the number of threads on the java application.
    String JVMProcessId;            // Is the process id of the java application.
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
    
//RunningContainer
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

    //////////////////////////////////////
    //  ACTUATOR GETTERS AND SETTERS    //
    //////////////////////////////////////
    
//JVMRamUsage
    public Long getJVMRamUsage() { return JVMRamUsage; }

    public void setJVMRamUsage(Long jVMRamUsage) { JVMRamUsage = jVMRamUsage; }

//JVMCpuUsagePerc
    public Long getJVMCpuUsagePerc() { return JVMCpuUsagePerc; }

    public void setJVMCpuUsagePerc(Long jVMCpuUsagePerc) { JVMCpuUsagePerc = jVMCpuUsagePerc; }

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
    
//JVMThreads
    public Integer getJVMThreads() { return JVMThreads; }

    public void setJVMThreads(Integer jVMThreads) { JVMThreads = jVMThreads; }

//JVMProcessId
    public String getJVMProcessId() { return JVMProcessId; }

    public void setJVMProcessId(String jVMProcessId) { JVMProcessId = jVMProcessId; }

//JVMProcessId
    public Boolean getJVMRunning() { return JVMRunning; }

    public void setJVMRunning(Boolean JVMRunning) { this.JVMRunning = JVMRunning; }

//Timestamp
    public Date getTimestamp() { return timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }



    
    /////////////////////////////////
    //      ERROR GET/SET HERE     //
    /////////////////////////////////


}
