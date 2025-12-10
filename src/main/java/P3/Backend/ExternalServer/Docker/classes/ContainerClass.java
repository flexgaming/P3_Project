package P3.Backend.ExternalServer.Docker.classes;

public class ContainerClass implements Cloneable {
    
    
    
    
    
    private String containerName;           
    private String containerId;             
    private Integer publicPort;             
    private Integer containerInterval;      
    private String region;          
    private String company;            
    private String server;          

    private LogsClass logs;

    
    private Boolean containerRunning;       
    private Long containerRamUsage;         
    private Double containerCpuUsage;         
    private Long containerDiskUsage;        
    private String containerStatus;         
    private Long containerPid;              
    private Long containerExitCode;         
    private int containerCpuCount;
    private Long containerRamLimit;

    
    
    private Boolean JVMRunning;             
    private Double JVMRamMax;               
    private Double JVMRamUsage;             
    private Double JVMCpuUsagePerc;         
    private Integer JVMThreads;             
    private Integer JVMThreadsStates;       
    private Integer JVMThreadQueued;       
    private Double JVMCpuUsageStart;         
    private Double JVMUptime;                
    
    
    private Long systemRamUsage;          
    private Long systemRamTotal;          
    private Double systemCpuUsage;
    private Double systemCpuUsagePerc;      
    private Integer systemCpuCores;         
    private Long systemDiskUsage;         
    private Long systemDiskTotal;         
    private Long systemDiskFree;          

    
    private Long timestamp;                 
    private Integer poolCore;             
    private Integer logbackEvents;        
    private Integer logbackEventsError;   
    private Integer logbackEventsWarn;    
    private Long garbageCollectSize;   
    
    

    
    private Double containerCpuPercent;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    
    
    

    public ContainerClass(String name, String id, Integer interval, Integer publicPort, String region, String company,
                          String server) {
        this.containerName = name;
        this.containerId = id;
        this.region = region;
        this.company = company;
        this.server = server;
        this.containerInterval = interval;
        this.publicPort = publicPort;
    }

    
    
    
    
    
    public String getContainerName() { return containerName; }
    
    public void setContainerName(String name) { this.containerName = name; }
    
    
    public String getContainerId() { return containerId; }
    
    public void setContainerId(String id) { this.containerId = id; }
    
    
    public Integer getPublicPort() { return publicPort; }
    
    public void setPublicPort(Integer publicPort) { this.publicPort = publicPort; }
    
    
    public Integer getContainerInterval() { return containerInterval; }
    
    public void setContainerInterval(Integer interval) { this.containerInterval = interval; }

    
    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }
    
    
    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    
    public String getServer() { return server; }
    
    public void setServer(String server) { this.server = server; }

    

    public LogsClass getLogs() { return logs; }

    public void setLogs(LogsClass logs) { this.logs = logs; }

    
    
    
    
    
    public Boolean getContainerRunning() { return containerRunning; }
    
    public void setContainerRunning(Boolean containerRunning) { this.containerRunning = containerRunning; }
    
    
    public Long getContainerRamUsage() { return containerRamUsage; }
    
    public void setContainerRamUsage(Long containerRamUsage) { this.containerRamUsage = containerRamUsage; }
    
    
    public Double getContainerCpuUsage() { return containerCpuUsage; }
    
    public void setContainerCpuUsage(Double containerCpuUsage) { this.containerCpuUsage = containerCpuUsage; }
    
    
    public Long getContainerDiskUsage() { return containerDiskUsage; }
    
    public void setContainerDiskUsage(Long containerDiskUsage) { this.containerDiskUsage = containerDiskUsage; }
    
    
    public String getContainerStatus() { return containerStatus; }
    
    public void setContainerStatus(String containerStatus) { this.containerStatus = containerStatus; }
    
    
    public Long getContainerPid() { return containerPid; }
    
    public void setContainerPid(Long containerPid) { this.containerPid = containerPid; }

    
    public Long getContainerExitCode() { return containerExitCode; }

    public void setContainerExitCode(Long containerExitCode) { this.containerExitCode = containerExitCode; }

    
    public int getContainerCpuCount() { return containerCpuCount; }

    public void setContainerCpuCount(int containerCpuCount) { this.containerCpuCount = containerCpuCount; }

    
    public Long getContainerRamLimit() { return containerRamLimit; }

    public void setContainerRamLimit(Long containerRamLimit) { this.containerRamLimit = containerRamLimit; }

    
    
    

    
    
    

    
    public Double getJVMRamMax() { return JVMRamMax; }

    public void setJVMRamMax(Double jVMRamMax) { JVMRamMax = jVMRamMax; }

    
    public Double getJVMRamUsage() { return JVMRamUsage; }

    public void setJVMRamUsage(Double jVMRamUsage) { JVMRamUsage = jVMRamUsage; }

    
    public Double getJVMCpuUsagePerc() { return JVMCpuUsagePerc; }

    public void setJVMCpuUsagePerc(Double jVMCpuUsagePerc) { JVMCpuUsagePerc = jVMCpuUsagePerc; }

    
    public Integer getJVMThreads() { return JVMThreads; }

    public void setJVMThreads(Integer jVMThreads) { JVMThreads = jVMThreads; }

    
    public Boolean getJVMRunning() { return JVMRunning; }

    public void setJVMRunning(Boolean JVMRunning) { this.JVMRunning = JVMRunning; }

    
    public Integer getJVMThreadsStates() { return JVMThreadsStates; }

    public void setJVMThreadsStates(Integer JVMThreadsStates) { this.JVMThreadsStates = JVMThreadsStates; }

    
    public Integer getJVMThreadQueued() { return JVMThreadQueued; }

    public void setJVMThreadQueued(Integer jVMThreadQueued) { JVMThreadQueued = jVMThreadQueued; }

    
    public Double getJVMCpuUsageStart() { return JVMCpuUsageStart; }

    public void setJVMCpuUsageStart(Double jVMCpuUsageStart) { JVMCpuUsageStart = jVMCpuUsageStart; }

    
    public Double getJVMUptime() { return JVMUptime; }

    public void setJVMUptime(Double jVMUptime) { JVMUptime = jVMUptime; }

    
    
    

    
    public Long getSystemRamUsage() { return systemRamUsage; }

    public void setSystemRamUsage(Long systemRamUsage) { this.systemRamUsage = systemRamUsage; }

    
    public Long getSystemRamTotal() { return systemRamTotal; }

    public void setSystemRamTotal(Long systemRamTotal) { this.systemRamTotal = systemRamTotal; }

    
    public Double getSystemCpuUsage() { return systemCpuUsage; }

    public void setSystemCpuUsage(Double systemCpuUsage) { this.systemCpuUsage = systemCpuUsage; }

    
    public Double getSystemCpuUsagePerc() { return systemCpuUsagePerc; }

    public void setSystemCpuUsagePerc(Double systemCpuUsagePerc) { this.systemCpuUsagePerc = systemCpuUsagePerc; }
    
    
    public Integer getSystemCpuCores() { return systemCpuCores; }

    public void setSystemCpuCores(Integer systemCpuCores) { this.systemCpuCores = systemCpuCores; }

    
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

    
    public Long getTimestamp() { return timestamp; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }

    
    public Double getContainerCpuPercent() { return containerCpuPercent; }

    public void setContainerCpuPercent(ContainerClass previousContainer) {
        if (previousContainer == null) {
            this.containerCpuPercent = null;
            return;
        }
        Double cpuDelta = this.containerCpuUsage - previousContainer.containerCpuUsage;
        Double systemDelta = this.systemCpuUsage - previousContainer.systemCpuUsage;
        if (systemDelta <= 0 || cpuDelta <= 0) this.containerCpuPercent = 0.0;
        else this.containerCpuPercent = (cpuDelta / systemDelta) * this.containerCpuCount;
    }
}
