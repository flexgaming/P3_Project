package P3.Backend.Docker.classes;

public class IntervalClass {

    private String id;
    private Integer interval;
    private Integer tempInterval;


    public IntervalClass(String id, Integer interval, Integer tempInterval) {
        this.id = id;
        this.interval = interval;
        this.tempInterval = tempInterval;
    }

    public String getId() {
        return id;
    }

    public void getId(String id) {
        this.id = id;
    }

    public Integer getInterval() {
        return interval;
    }

    public void getInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getTempInterval() {
        return tempInterval;
    }

    public void setTempInterval(Integer tempInterval) {
        this.tempInterval = tempInterval;
    }
}
