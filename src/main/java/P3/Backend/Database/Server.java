package P3.Backend.Database;

import java.util.ArrayList;

public class Server {
    private final String serverID;
    private final double ramTotal;
    private final double cpuTotal;
    private final double diskUsageTotal;

    private final ArrayList<Container> containers = new ArrayList<>();

    public Server(String serverID, double ramTotal, double cpuTotal, double diskUsageTotal) {
        this.serverID = serverID;
        this.ramTotal = ramTotal;
        this.cpuTotal = cpuTotal;
        this.diskUsageTotal = diskUsageTotal;
    }

    public void addContainer(Container container) {
        containers.add(container);
    }

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

    public ArrayList<Container> getContainers() {
        return containers;
    }

    public Container getContainer(String containerID) {
        for (Container container : containers) {
            if (container.getContainerID().equals(containerID)) {
                return container;
            }
        }

        return null;
    }
}
