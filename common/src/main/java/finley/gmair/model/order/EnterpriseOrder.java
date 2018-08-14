package finley.gmair.model.order;

import finley.gmair.model.Entity;

public class EnterpriseOrder extends Entity {
    private String orderId;
    private String merchantId;
    private String modelName;
    private boolean requireInstall;
    private boolean planConfirmed;
    private String description;

    public EnterpriseOrder() {
    }

    public EnterpriseOrder(String merchantId, String modelName, boolean requireInstall, boolean planConfirmed, String description) {
        this.merchantId = merchantId;
        this.modelName = modelName;
        this.requireInstall = requireInstall;
        this.planConfirmed = planConfirmed;
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isRequireInstall() {
        return requireInstall;
    }

    public void setRequireInstall(boolean requireInstall) {
        this.requireInstall = requireInstall;
    }

    public boolean isPlanConfirmed() {
        return planConfirmed;
    }

    public void setPlanConfirmed(boolean planConfirmed) {
        this.planConfirmed = planConfirmed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
