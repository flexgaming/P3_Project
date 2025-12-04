package P3.Backend.Docker.classes;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LogsClass {
    private List<String> error = new ArrayList<>();
    private List<String> warn = new ArrayList<>();

    //@JsonProperty("Info")
    // private List<String> info = new ArrayList<>();

    public List<String> getError() { return error; }
    public void setError(List<String> error) { this.error = error != null ? error : new ArrayList<>(); }

    public List<String> getWarn() { return warn; }
    public void setWarn(List<String> warn) { this.warn = warn != null ? warn : new ArrayList<>(); }

    // public List<String> getInfo() { return info; }
    // public void setInfo(List<String> info) { this.info = info != null ? info : new ArrayList<>(); }
}
