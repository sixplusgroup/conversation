package finley.gmair.model.express;

import finley.gmair.model.Entity;

public class ExpressParcel extends Entity {

    private String expressId;

    private String parentExpress;

    private String expressNo;

    private String codeValue;

    private ExpressStatus expressStatus;

    private ParcelType parcelType;

    public ExpressParcel() { super(); }

    public ExpressParcel(String parent_express, String expressNo, String codeValue, ParcelType parcelType){
        this();
        this.parentExpress = parent_express;
        this.expressNo = expressNo;
        this.codeValue = codeValue;
        this.expressStatus = ExpressStatus.ASSIGNED;
        this.parcelType = parcelType;
    }

    public String getExpressId() {
        return expressId;
    }

    public void setExpressId(String expressId) {
        this.expressId = expressId;
    }

    public String getParentExpress() {
        return parentExpress;
    }

    public void setParentExpress(String parentExpress) {
        this.parentExpress = parentExpress;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public ExpressStatus getExpressStatus() {
        return expressStatus;
    }

    public void setExpressStatus(ExpressStatus expressStatus) {
        this.expressStatus = expressStatus;
    }

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }
}
