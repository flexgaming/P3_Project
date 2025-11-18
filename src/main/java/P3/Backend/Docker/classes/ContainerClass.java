package P3.Backend.Docker.classes;

import java.util.Date;

public class ContainerClass {
    
    // All of the variables that we want to store from each container.
    // That is also sent to the database.
    
    Date timestamp;                 // Is the time when data is fetched.
    
    // Data from JSON:
    String containerName;           // Is the name of the docker container.
    String containerId;             // Is the id of the docker container.
    Integer containerInterval;      // Is the interval for sending data to another server. 

    // Data from docker:
    Boolean runningContainer;       // Is docker container on or off.
    Long containerRamUsage;       // Is ram usage on docker container.
    Long containerCpuUsage;       // Is cpu usage on docker container.
    Long containerDiskUsage;      // Is disk usage on docker container.
    String containerStatus;         // Is the status of the docker container.

    
    // Data from actuator:
    Boolean runningJVM;             // Is internally java application on or off.
    Double JVMRamUsage;             // Is ram usage on java application.
    Double JVMCpuUsagePerc;         // Is cpu usage on java application in percentage.
    Double systemRamUsage;          // Is all of the ram usage on the system.
    Double systemCpuUsagePerc;      // Is all of the cpu usage on the system in percentage.
    Integer SystemCpuCores;         // Is the number of cpu cores on the system.
    Double systemDiskUsage;         // Is all of the disk usage on the system.
    Double systemDiskTotal;         // Is the total disk space on the system.
    Double systemDiskFree;          // Is the free disk space on the system.
    Integer JVMThreads;             // Is the number of threads on the java application.
    String JVMProcessId;            // Is the process id of the java application.
    //String JVMStatus;               // Is the status of the java application.
    
    

    /////////////////////////
    //      ERROR HERE     //
    /////////////////////////

    public ContainerClass(String name, String id, Integer interval) {
        this.containerName = name;
        this.containerId = id;
        this.containerInterval = interval;
    }

    ////////////////////////////////
    //  JSON GETTERS AND SETTERS  //
    ////////////////////////////////
    
//ContainerName
    public String getContainerName() { return containerName; }

    public String getContainerId() { return containerId; }

//ContainerInterval
    public Integer getContainerInterval() { return containerInterval; }

    public void setContainerName(String name) { this.containerName = name; }

//ContainerId
    public void setContainerId(String id) { this.containerId = id; }
    
    public void setContainerInterval(Integer interval) { this.containerInterval = interval; }

    //////////////////////////////////////
    //  CONTAINER GETTERS AND SETTERS   //
    //////////////////////////////////////
    
//RunningContainer
    public Boolean getRunningContainer() { return runningContainer; }

    public void setRunningContainer(Boolean runningContainer) { this.runningContainer = runningContainer; }

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
    public Double getJVMRamUsage() { return JVMRamUsage; }

    public void setJVMRamUsage(Double jVMRamUsage) { JVMRamUsage = jVMRamUsage; }

//JVMCpuUsagePerc
    public Double getJVMCpuUsagePerc() { return JVMCpuUsagePerc; }

    public void setJVMCpuUsagePerc(Double jVMCpuUsagePerc) { JVMCpuUsagePerc = jVMCpuUsagePerc; }

//SystemRamUsage
    public Double getSystemRamUsage() { return systemRamUsage; }

    public void setSystemRamUsage(Double systemRamUsage) { this.systemRamUsage = systemRamUsage; }

//SystemCpuUsagePerc
    public Double getSystemCpuUsagePerc() { return systemCpuUsagePerc; }

    public void setSystemCpuUsagePerc(Double systemCpuUsagePerc) { this.systemCpuUsagePerc = systemCpuUsagePerc; }
    
//SystemCpuCores
    public Integer getSystemCpuCores() { return SystemCpuCores; }

    public void setSystemCpuCores(Integer systemCpuCores) { SystemCpuCores = systemCpuCores; }

//SystemDiskUsage
    public Double getSystemDiskUsage() { return systemDiskUsage; }

    public void setSystemDiskUsage(Double systemDiskUsage) { this.systemDiskUsage = systemDiskUsage; }

//SystemDiskTotal
    public Double getSystemDiskTotal() { return systemDiskTotal; }

    public void setSystemDiskTotal(Double systemDiskTotal) { this.systemDiskTotal = systemDiskTotal; }

//SystemDiskFree
    public Double getSystemDiskFree() { return systemDiskFree; }

    public void setSystemDiskFree(Double systemDiskFree) { this.systemDiskFree = systemDiskFree; }
    
//JVMThreads
    public Integer getJVMThreads() { return JVMThreads; }

    public void setJVMThreads(Integer jVMThreads) { JVMThreads = jVMThreads; }

//JVMProcessId
    public String getJVMProcessId() { return JVMProcessId; }

    public void setJVMProcessId(String jVMProcessId) { JVMProcessId = jVMProcessId; }

    /////////////////////////////////
    //      ERROR GET/SET HERE     //
    /////////////////////////////////


}
