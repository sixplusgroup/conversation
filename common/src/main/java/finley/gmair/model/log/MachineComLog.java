package finley.gmair.model.log;

import java.sql.Timestamp;

public class MachineComLog extends AbstractLog {
    private String uid;

    private String action;

    public MachineComLog(String uid, String action, String logDetail, String ip, Timestamp createAt) {
        super(logDetail, ip, createAt);
        this.uid = uid;
        this.action = action;
    }
}
