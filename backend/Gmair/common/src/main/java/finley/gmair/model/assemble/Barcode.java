package finley.gmair.model.assemble;

import finley.gmair.model.Entity;

public class Barcode extends Entity {
    private String codeId;
    private String codeValue;

    public Barcode() {
        super();
    }

    public Barcode(String codeValue) {
        super();
        this.codeValue = codeValue;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }
}
