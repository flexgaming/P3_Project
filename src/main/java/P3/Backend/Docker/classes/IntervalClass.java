package P3.Backend.Docker.classes;

public class IntervalClass {

    private String id;
    private Integer interval;
    private Integer tempInterval;

    /** This function is used to construct the class with a few parameters.
     * 
     * @param id Is used to identify which container that this intervalClass belongs to.
     * @param interval Is used in order to remember the original interval.
     * @param tempInterval Is used to count down until 0 and then set to the original interval again.
     */
    public IntervalClass(String id, Integer interval, Integer tempInterval) {
        this.id = id;
        this.interval = interval;
        this.tempInterval = tempInterval;
    }

    // Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Interval
    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    // TempInterval
    public Integer getTempInterval() {
        return tempInterval;
    }

    public void setTempInterval(Integer tempInterval) {
        this.tempInterval = tempInterval;
    }
}
