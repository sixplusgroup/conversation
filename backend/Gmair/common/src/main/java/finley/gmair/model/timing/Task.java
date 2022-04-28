package finley.gmair.model.timing;

import finley.gmair.model.Entity;

public class Task extends Entity {
    private String taskId;

    private String taskName;

    private String frequent;

    private String description;

    private boolean status;

    public Task() {
        super();
        this.status = true;
    }

    public Task(String taskName, String frequent, String description) {
        this();
        this.taskName = taskName;
        this.frequent = frequent;
        this.description = description;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getFrequent() {
        return frequent;
    }

    public void setFrequent(String frequent) {
        this.frequent = frequent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
