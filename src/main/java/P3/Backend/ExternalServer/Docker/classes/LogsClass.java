package P3.Backend.ExternalServer.Docker.classes;

import java.util.ArrayList;
import java.util.List;

public class LogsClass {
    private List<String> error = new ArrayList<>();
    private List<String> warn = new ArrayList<>();

    public List<String> getError() { return error; }

    public void setError(List<String> error) { this.error = error != null ? error : new ArrayList<>(); }

    public List<String> getWarn() { return warn; }

    public void setWarn(List<String> warn) { this.warn = warn != null ? warn : new ArrayList<>(); }
}