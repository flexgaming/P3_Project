package P3.Backend.Database;

import java.util.ArrayList;

public class Container {
    private String containerID;

    private ArrayList<Diagnostics> diagnosticsData = new ArrayList<>();

    public Container(String containerID) {
        this.containerID = containerID;
    }

    public void addDiagnostics(Diagnostics diagnostics) {
        diagnosticsData.add(diagnostics);
    }

    public String getContainerID() {
        return containerID;
    }

    public ArrayList<Diagnostics> getDiagnosticsData() {
        return diagnosticsData;
    }
}
