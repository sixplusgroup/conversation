package finley.gmair.model.drift;

import finley.gmair.model.Entity;

public class DriftOrderAction extends Entity {

    private String actionId;

    private String orderId;

    private String message;

    private String member;

    public DriftOrderAction(){
        super();
    }

    public DriftOrderAction(String orderId,String message,String member){
        this();
        this.orderId=orderId;
        this.message=message;
        this.member=member;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
