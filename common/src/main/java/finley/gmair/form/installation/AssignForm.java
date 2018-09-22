package finley.gmair.form.installation;

public class AssignForm {

    private String qrcode;

    private String consumerConsignee;

    private String consumerPhone;

    private String consumerAddress;

    public AssignForm(String qrcode, String consumerConsignee, String consumerPhone, String consumerAddress) {
        this.qrcode = qrcode;
        this.consumerConsignee = consumerConsignee;
        this.consumerPhone = consumerPhone;
        this.consumerAddress = consumerAddress;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getConsumerConsignee() {
        return consumerConsignee;
    }

    public void setConsumerConsignee(String consumerConsignee) {
        this.consumerConsignee = consumerConsignee;
    }

    public String getConsumerPhone() {
        return consumerPhone;
    }

    public void setConsumerPhone(String consumerPhone) {
        this.consumerPhone = consumerPhone;
    }

    public String getConsumerAddress() {
        return consumerAddress;
    }

    public void setConsumerAddress(String consumerAddress) {
        this.consumerAddress = consumerAddress;
    }
}
