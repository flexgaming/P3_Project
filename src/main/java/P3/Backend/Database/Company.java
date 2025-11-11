package P3.Backend.Database;

import java.util.ArrayList;

public class Company {
    private final String companyID;
    private final String companyName;

    private final ArrayList<Server> servers = new ArrayList<>();

    public Company(String companyID, String companyName) {
        this.companyID = companyID;
        this.companyName = companyName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    /**
     * Get all servers in this company.
     * @return All servers in the company.
     */
    public ArrayList<Server> getServers() {
        return servers;
    }

    /**
     * Search for a server using the server ID.
     * @param serverID The ID of the server being searched for.
     * @return The Server object if it exists, otherwise null.
     */
    public Server getServer(String serverID) {
        for (Server server : servers) {
            if (server.getServerID().equals(serverID)) {
                return server;
            }
        }

        return null;
    }

    /**
     * Adds a Server object to this company.
     * @param server The Server object being added to the company.
     */
    public void addServer(Server server) {
        servers.add(server);
    }
}
