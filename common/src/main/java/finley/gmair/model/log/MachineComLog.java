package finley.gmair.model.log;

import java.sql.Timestamp;

public class MachineComLog extends AbstractLog {

    private String uid;

    private String action;

    public MachineComLog(String uid, String action, String logDetail, String ip) {
        super(logDetail, ip);
        this.uid = uid;
        this.action = action;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
