package finley.gmair.form.drift;

public class ActivityForm {
    private String activityName;

    private int repositorySize;

    private double threshold;

    private int reservableDays;

    private String startTime;

    private String endTime;

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getRepositorySize() {
        return repositorySize;
    }

    public void setRepositorySize(int repositorySize) {
        this.repositorySize = repositorySize;
    }

    public double getThreshold() {
        return threshold;
    }

    public int getReservableDays() {
        return reservableDays;
    }

    public void setReservableDays(int reservableDays) {
        this.reservableDays = reservableDays;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
