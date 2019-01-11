package finley.gmair.vo.machine;

import java.sql.Timestamp;

public class MachineInfoVo {
    private String codeValue;
    private String machineId;
    private String bindName;
    private String consumerName;
    private String consumerPhone;
    private String consumerProvince;
    private String consumerCity;
    private String consumerAddress;
    private boolean isOnline;
    private Timestamp bindTime;
    private int overCount;

    public MachineInfoVo(String codeValue, String machineId, String bindName, String consumerName, String consumerPhone, String consumerProvince, String consumerCity, String consumerAddress, boolean isOnline, Timestamp bindTime, int overCount) {
        this.codeValue = codeValue;
        this.machineId = machineId;
        this.bindName = bindName;
        this.consumerName = consumerName;
        this.consumerPhone = consumerPhone;
        this.consumerProvince = consumerProvince;
        this.consumerCity = consumerCity;
        this.consumerAddress = consumerAddress;
        this.isOnline = isOnline;
        this.bindTime = bindTime;
        this.overCount = overCount;
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

    public String getBindName() {
        return bindName;
    }

    public void setBindName(String bindName) {
        this.bindName = bindName;
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

    public String getConsumerProvince() {
        return consumerProvince;
    }

    public void setConsumerProvince(String consumerProvince) {
        this.consumerProvince = consumerProvince;
    }

    public String getConsumerCity() {
        return consumerCity;
    }

    public void setConsumerCity(String consumerCity) {
        this.consumerCity = consumerCity;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public Timestamp getBindTime() {
        return bindTime;
    }

    public void setBindTime(Timestamp bindTime) {
        this.bindTime = bindTime;
    }

    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }
}
