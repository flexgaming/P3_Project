package P3.Backend.Database;

import java.util.ArrayList;

public class Container {
    private final String containerID;

    private final ArrayList<Diagnostics> diagnosticsData = new ArrayList<>();

    public Container(String containerID) {
        this.containerID = containerID;
    }

    public String getContainerID() {
        return containerID;
    }

    /**
     * Get all diagnostics data in this container.
     * @return All diagnostics data in the container.
     */
    public ArrayList<Diagnostics> getDiagnosticsData() {
        return diagnosticsData;
    }

    /**
     * Adds a Diagnostics object to this container.
     * @param diagnostics The Diagnostics object being added to the container.
     */
    public void addDiagnostics(Diagnostics diagnostics) {
        diagnosticsData.add(diagnostics);
    }
}
