package finley.gmair.vo.machine;

import java.sql.Timestamp;

public class MachineInfoVo {
    private String consumerId;
    private String bindName;
    private String codeValue;
    private String machineId;
    private String consumerName;
    private String consumerPhone;
    private boolean blockFlag;
    private Timestamp bindTime;

    public MachineInfoVo(String consumerId, String bindName, String codeValue, String machineId, String consumerName, String consumerPhone, boolean blockFlag, Timestamp bindTime) {
        this.consumerId = consumerId;
        this.bindName = bindName;
        this.codeValue = codeValue;
        this.machineId = machineId;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.blockFlag = blockFlag;
        this.bindTime = bindTime;
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

    public boolean isBlockFlag() {
        return blockFlag;
    }

    public void setBlockFlag(boolean blockFlag) {
        this.blockFlag = blockFlag;
    }

    public Timestamp getBindTime() {
        return bindTime;
    }

    public void setBindTime(Timestamp bindTime) {
        this.bindTime = bindTime;
    }
}
