package finley.gmair.form.message;

public class MessageTemplateForm {
    private String text;

    private int catalog;

    public MessageTemplateForm() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCatalog() {
        return catalog;
    }

    public void setCatalog(int catalog) {
        this.catalog = catalog;
    }
}
