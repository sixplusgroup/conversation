package finley.gmair.model.mqttManagement;

import lombok.Data;

/**
 * 消息内容的一条条的属性
 *
 * @author lycheeshell
 * @date 2020/12/3 22:43
 */
@Data
public class Attribute {

    /**
     * 属性id
     */
    private String attributeId;

    /**
     * 属性标示名称英文字符串
     */
    private String name;

    /**
     * 属性描述
     */
    private String description;

    /**
     * 该属性是否必须有
     */
    private Boolean required;
}
