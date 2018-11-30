package finley.gmair.model.timing;

import finley.gmair.model.Entity;

public class Task extends Entity {
    private String taskId;

    private String taskName;

    private boolean status;

    public Task() {
        super();
        this.status = true;
    }

    public Task(String taskName) {
        this();
        this.taskName = taskName;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
