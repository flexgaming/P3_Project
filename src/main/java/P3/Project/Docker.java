package P3.Project;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Docker {
    private String id;
    private String name;
    
    // Constructor
    public Docker() {}
    public Docker(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public void setId(String id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public String getId(){return id;}
    public String getName() {return name;}

}
