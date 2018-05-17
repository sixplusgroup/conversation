package finley.gmair.model.log;

import java.sql.Timestamp;

public abstract class AbstractLog {
    private String logDetail;

    private String ip;

    private long createAt;

    public AbstractLog(String logDetail, String ip) {
        this.logDetail = logDetail;
        this.ip = ip;
        this.createAt = System.currentTimeMillis();
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

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
