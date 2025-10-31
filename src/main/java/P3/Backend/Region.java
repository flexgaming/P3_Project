package P3.Backend;

public class Region {
    private int regionID;
    private String name;

    private Company[] companies;

    public Region(int regionID, String name) {
        this.regionID = regionID;
        this.name = name;
    }

    public int getRegionID() {
        return regionID;
    }

    public String getRegionName() {
        return name;
    }
}
