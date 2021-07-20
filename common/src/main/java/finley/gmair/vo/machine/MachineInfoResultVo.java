package finley.gmair.vo.machine;

import java.sql.Timestamp;

public class MachineInfoResultVo extends MachineInfoVo {
    private int overCount;
    private boolean isOnline;

    public MachineInfoResultVo(String consumerId, String bindName, String codeValue, String machineId, String consumerName, String consumerPhone, boolean blockFlag, Timestamp bindTime, int overCount, boolean isOnline) {
        super(consumerId, bindName, codeValue, machineId, consumerName, consumerPhone, blockFlag, bindTime);
        this.overCount = overCount;
        this.isOnline = isOnline;
    }

    public int getOverCount() {
        return overCount;
    }

    public void setOverCount(int overCount) {
        this.overCount = overCount;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
