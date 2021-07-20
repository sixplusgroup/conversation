package finley.gmair.model.installation;

import finley.gmair.model.Entity;

/**
 * @ClassName: AssignAction
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 2:30 PM
 */
public class AssignAction extends Entity {
    private String actionId;

    private String assignId;

    private String message;

    public AssignAction() {
        super();
    }

    public AssignAction(String assignId, String message) {
        this();
        this.assignId = assignId;
        this.message = message;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getAssignId() {
        return assignId;
    }

    public void setAssignId(String assignId) {
        this.assignId = assignId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
