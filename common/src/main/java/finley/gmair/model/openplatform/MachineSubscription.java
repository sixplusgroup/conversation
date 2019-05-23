package finley.gmair.model.openplatform;

import finley.gmair.model.Entity;

/**
 * @ClassName: MachineSubscription
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/7 5:27 PM
 */
public class MachineSubscription extends Entity {
    private String subscriptionId;

    private String corpId;

    private String qrcode;

    public MachineSubscription() {
        super();
    }

    public MachineSubscription(String corpId, String qrcode) {
        this.corpId = corpId;
        this.qrcode = qrcode;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
