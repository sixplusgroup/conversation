package finley.gmair.model.enterpriseselling;

import finley.gmair.model.Entity;

public class MerchantContact extends Entity {
    private String contactId;
    private String merchantId;
    private String contactName;
    private String contactPhone;
    private boolean preferred;

    public MerchantContact(){
        super();
    }
    public MerchantContact(String merchantId, String contactName, String contactPhone, boolean preferred) {
        super();
        this.merchantId = merchantId;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.preferred = preferred;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
