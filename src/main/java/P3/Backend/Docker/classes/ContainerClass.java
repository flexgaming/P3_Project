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
    Double containerRamUsage;       // Is ram usage on docker container.
    Double containerCpuUsage;       // Is cpu usage on docker container.
    Double containerDiskUsage;      // Is disk usage on docker container.
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

    public String getcontainerName() {
        return containerName;
    }

    public String getcontainerId() {
        return containerId;
    }

    public Integer getcontainerInterval() {
        return containerInterval;
    }


}
