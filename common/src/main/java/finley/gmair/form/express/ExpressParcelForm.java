package finley.gmair.form.express;

import finley.gmair.model.express.ParcelType;

public class ExpressParcelForm {
    private String parentExpress;

    private String expressNo;

    private String codeValue;

    private ParcelType parcelType;

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

    public ParcelType getParcelType() {
        return parcelType;
    }

    public void setParcelType(ParcelType parcelType) {
        this.parcelType = parcelType;
    }
}
