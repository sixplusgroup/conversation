package finley.gmair.form.message;

public class MessageForm {
    private String phone;

    private String text;

    private String signature;

    public MessageForm() {
        super();
    }

    public MessageForm(String phone, String text) {
        this();
        this.phone = phone;
        this.text = text;
    }

    public MessageForm(String phone, String text,String signature) {
        this(phone,text);
        this.signature = signature;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
