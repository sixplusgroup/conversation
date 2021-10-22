package finley.gmair.model.mqttManagement;

import finley.gmair.model.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * 消息的主题
 *
 * @author lycheeshell
 * @date 2020/12/3 22:23
 */
@Getter
@Setter
public class Topic extends Entity {

    /**
     * 主题id
     */
    private String topicId;

    /**
     * 主题格式，正则表达式
     */
    private String topicDetail;

    /**
     * 主题描述说明
     */
    private String topicDescription;

    public Topic() {
        super();
    }

    public Topic(String topicDetail, String topicDescription) {
        this();
        this.topicDetail = topicDetail;
        this.topicDescription = topicDescription;
    }

}
