package finley.gmair.model.mqttManagement;

import finley.gmair.model.Entity;
import lombok.Data;

import java.util.List;

/**
 * 消息请求的行为
 *
 * @author lycheeshell
 * @date 2020/12/3 22:28
 */
@Data
public class Action extends Entity {

    /**
     * 行为id
     */
    private String actionId;

    /**
     * 行为标识名称英文字符串
     */
    private String name;

    /**
     * 行为描述说明
     */
    private String description;

    /**
     * 行文可以包含哪些属性
     */
    private List<Attribute> attributes;

}
