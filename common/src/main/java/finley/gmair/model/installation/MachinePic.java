package finley.gmair.model.installation;

import finley.gmair.model.Entity;

public class MachinePic extends Entity {
    private String recordId;
    private String codeValue;
    private String picUrl;

    public MachinePic() {
        super();
    }

    public MachinePic(String codeValue, String picUrl) {
        super();
        this.codeValue = codeValue;
        this.picUrl = picUrl;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
