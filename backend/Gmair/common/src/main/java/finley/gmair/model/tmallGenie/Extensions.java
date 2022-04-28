package finley.gmair.model.tmallGenie;

public class Extensions {

    String request_token;

    String extension2;

    String extension1;

    public String getExtension1() {
        return extension1;
    }

    public void setExtension1(String extension1) {
        this.extension1 = extension1;
    }

    public Extensions() {
        super();
    }

    /**
     * 用于设备发现的构造函数
     * @param extension1
     * @param extension2
     */
    public Extensions(String extension1, String extension2) {
        this.extension1 = extension1;
        this.extension2 = extension2;
    }

    public Extensions(String request_token, String extension1, String extension2) {
        this.request_token = request_token;
        this.extension1 = extension1;
        this.extension2 = extension2;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }

    public String getExtension2() {
        return extension2;
    }

    public void setExtension2(String extension2) {
        this.extension2 = extension2;
    }
}
