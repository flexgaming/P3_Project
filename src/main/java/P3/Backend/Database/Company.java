package P3.Backend.Database;

import java.util.ArrayList;

public class Company {
    private final int companyID;
    private final String name;

    private ArrayList<Server> servers = new ArrayList<>();

    public Company(int companyID, String name) {
        this.companyID = companyID;
        this.name = name;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public int getCompanyID() {
        return companyID;
    }

    public String getCompanyName() {
        return name;
    }

    public ArrayList<Server> getServers() {
        return servers;
    }

    public Server getServer(String serverID) {
        for (Server server : servers) {
            if (server.getServerID().equals(serverID)) {
                return server;
            }
        }

        return null;
    }
}
