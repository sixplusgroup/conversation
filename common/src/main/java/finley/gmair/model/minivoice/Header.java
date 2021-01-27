package finley.gmair.model.minivoice;

public class Header {

    String namespace;

    String name;

    String messageId;

    int payLoadVersion = 1;

    public Header() {
        super();
    }

    public Header(String namespace, String name, String messageId, int payLoadVersion) {
        this.namespace = namespace;
        this.name = name;
        this.messageId = messageId;
        this.payLoadVersion = payLoadVersion;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getPayLoadVersion() {
        return payLoadVersion;
    }

    public void setPayLoadVersion(int payLoadVersion) {
        this.payLoadVersion = payLoadVersion;
    }

    @Override
    public String toString() {
        return "Header{" +
                "namespace='" + namespace + '\'' +
                ", name='" + name + '\'' +
                ", messageId='" + messageId + '\'' +
                ", payLoadVersion=" + payLoadVersion +
                '}';
    }

}
