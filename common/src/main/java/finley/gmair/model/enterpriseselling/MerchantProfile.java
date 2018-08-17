package finley.gmair.model.enterpriseselling;

import finley.gmair.model.Entity;

public class MerchantProfile extends Entity {
    private String merchantId;
    private String merchantName;
    private String merchantCode;
    private String merchantAddress;

    public MerchantProfile() {
        super();
    }

    public MerchantProfile(String merchantName, String merchantCode, String merchantAddress) {
        super();
        this.merchantName = merchantName;
        this.merchantCode = merchantCode;
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }
}
