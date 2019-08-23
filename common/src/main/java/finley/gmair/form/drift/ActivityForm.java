package finley.gmair.form.drift;

public class ActivityForm {
    private String activityName;

    private int repositorySize;

    private double threshold;

    private int reservableDays;

    private String startTime;

    private String endTime;

    private String openDate;

    private String closeDate;

    private int delayDays;

    private String introduction;

    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

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

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public int getDelayDays() {
        return delayDays;
    }

    public void setDelayDays(int delayDays) {
        this.delayDays = delayDays;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
