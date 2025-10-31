package P3.Backend;

public class Server {
    private String serverID;
    private double ramTotal;
    private double cpuTotal;
    private double diskUsageTotal;

    private Container[] containers;

    public String getServerID() {
        return serverID;
    }

    public double getRamTotal() {
        return ramTotal;
    }

    public double getCpuTotal() {
        return cpuTotal;
    }

    public double getDiskUsageTotal() {
        return diskUsageTotal;
    }
}
