package finley.gmair.model.machine;

import finley.gmair.model.Entity;

/**
 * @author ：CK
 * @date ：Created in 2020/11/1 22:45
 * @description：
 */
public class Text extends Entity {

    /**
     * 文字ID
     */
    private String textId;

    /**
     * 文字类型
     */
    private String textType;

    /**
     * 文字内容
     */
    private String textContent;

    public Text(String textId, String textType, String textContent) {
        this.textId = textId;
        this.textType = textType;
        this.textContent = textContent;
    }

    public Text() {
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public String getTextType() {
        return textType;
    }

    public void setTextType(String textType) {
        this.textType = textType;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }
}
