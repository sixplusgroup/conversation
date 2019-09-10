package finley.gmair.model.drift;


import finley.gmair.model.Entity;

import java.sql.Timestamp;

public class ExpressData extends Entity {
    private String detailId;

    private String expressId;

    private String context;

    private Timestamp time;

    private String status;

    public ExpressData(){}

    public ExpressData(String detailId, String expressId, String context,Timestamp time,String status){
        this.detailId=detailId;
        this.expressId=expressId;
        this.context=context;
        this.time=time;
        this.status=status;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
