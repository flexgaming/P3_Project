package P3.Backend.Docker.classes;

import java.util.ArrayList;
import java.util.List;

public class LogsClass {
    // Add two lists for error and warn.
    private List<String> error = new ArrayList<>();
    private List<String> warn = new ArrayList<>();

    // Error
    public List<String> getError() { return error; }

    public void setError(List<String> error) { this.error = error != null ? error : new ArrayList<>(); }

    // Warn
    public List<String> getWarn() { return warn; }

    public void setWarn(List<String> warn) { this.warn = warn != null ? warn : new ArrayList<>(); }
}
