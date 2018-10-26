package finley.gmair.model.drift;

import finley.gmair.model.Entity;

import java.util.Date;

public class Activity extends Entity {
    private String activityId;

    private String activityName;

    private int repositorySize;

    private double threshold;

    private int reservableDays;

    private Date startTime;

    private Date endTime;

    private String introduction;

    public Activity() {
        super();
    }

    public Activity(String activityName, int repositorySize, double threshold, int reservableDays, Date startTime, Date endTime, String introduction) {
        this();
        this.activityName = activityName;
        this.repositorySize = repositorySize;
        this.threshold = threshold;
        this.reservableDays = reservableDays;
        this.startTime = startTime;
        this.endTime = endTime;
        this.introduction = introduction;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
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

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public int getReservableDays() {
        return reservableDays;
    }

    public void setReservableDays(int reservableDays) {
        this.reservableDays = reservableDays;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
