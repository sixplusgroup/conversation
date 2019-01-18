package finley.gmair.model.machine;

import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class MachineSecondListView extends Entity {
    private String consumerId;
    private String bindName;
    private String codeValue;
    private String machineId;
    private String consumerName;
    private String consumerPhone;
    private int overCount;
    private Timestamp outPm25Time;

    public MachineSecondListView() {
        super();
    }

    public MachineSecondListView(String consumerId, String bindName, String codeValue, String machineId, String consumerName, String consumerPhone, int overCount, Timestamp outPm25Time) {
        super();
        this.consumerId = consumerId;
        this.bindName = bindName;
        this.codeValue = codeValue;
        this.machineId = machineId;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.overCount = overCount;
        this.outPm25Time = outPm25Time;
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

    public Timestamp getOutPm25Time() {
        return outPm25Time;
    }

    public void setOutPm25Time(Timestamp outPm25Time) {
        this.outPm25Time = outPm25Time;
    }
}
