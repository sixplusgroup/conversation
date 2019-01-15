package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class MachineListDaily extends Entity {
    private String consumerId;
    private String bindName;
    private String codeValue;
    private String machineId;
    private String consumerName;
    private String consumerPhone;
    private int overCount;
    private boolean offline;

    public MachineListDaily() {
        super();
    }

    public MachineListDaily(String consumerId, String bindName, String codeValue, String machineId, String consumerName, String consumerPhone, int overCount, boolean offline) {
        super();
        this.consumerId = consumerId;
        this.bindName = bindName;
        this.codeValue = codeValue;
        this.machineId = machineId;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.overCount = overCount;
        this.offline = offline;
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

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getConsumerName() {
        return consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}
