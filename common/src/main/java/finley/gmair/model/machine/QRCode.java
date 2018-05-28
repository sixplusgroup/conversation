package finley.gmair.model.machine;

import finley.gmair.model.Entity;

public class QRCode extends Entity {
    private String codeId;

    private String modelId;

    private String batchValue;

    private String codeValue;

    private String codeUrl;

    private QRCodeStatus status;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getBatchValue() {
        return batchValue;
    }

    public void setBatchValue(String batchValue) {
        this.batchValue = batchValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public QRCodeStatus getStatus() {
        return status;
    }

    public void setStatus(QRCodeStatus status) {
        this.status = status;
    }
}
