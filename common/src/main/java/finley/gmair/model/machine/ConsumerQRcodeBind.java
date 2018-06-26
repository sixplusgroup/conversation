package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class ConsumerQRcodeBind extends Entity {
    private String bindId;
    private String consumerId;
    private String bindName;
    private String codeValue;
    private Ownership ownership;

    public ConsumerQRcodeBind(){
        super();
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }
}
