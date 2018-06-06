package finley.gmair.form.express;

public class ExpressParcelForm {
    private String parentExpress;

    private String expressNo;

    private String codeValue;

    private int parcelType;

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

    public int getParcelType() {
        return parcelType;
    }

    public void setParcelType(int parcelType) {
        this.parcelType = parcelType;
    }
}
