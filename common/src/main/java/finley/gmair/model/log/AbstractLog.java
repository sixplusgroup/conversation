package finley.gmair.model.log;

import java.sql.Timestamp;

public abstract class AbstractLog {
    private String logDetail;

    private String ip;

    private Timestamp createAt;

    public AbstractLog(String logDetail, String ip) {
        this.logDetail = logDetail;
        this.ip = ip;
        this.createAt = new Timestamp(System.currentTimeMillis());
    }

    public String getLogDetail() {
        return logDetail;
    }

    public void setLogDetail(String logDetail) {
        this.logDetail = logDetail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
