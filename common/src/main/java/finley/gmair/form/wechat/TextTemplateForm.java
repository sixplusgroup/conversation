package finley.gmair.form.wechat;

public class TextTemplateForm {

    private String inMessageType;

    private String keyword;

    private String response;

    public TextTemplateForm() {
        super();
    }

    public String getInMessageType() {
        return inMessageType;
    }

    public void setInMessageType(String inMessageType) {
        this.inMessageType = inMessageType;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
