package finley.gmair.service;

import finley.gmair.model.mqttManagement.Topic;

import java.util.List;

/**
 * 消息主题服务
 *
 * @author lycheeshell
 * @date 2020/12/15 16:11
 */
public interface TopicService {

    /**
     * 新增主题
     *
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 新增行数
     */
    int addTopic(String topicDetail, String topicDescription);

    /**
     * 更新主题
     *
     * @param topicId          主题id
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 更新行数
     */
    int modifyTopic(String topicId, String topicDetail, String topicDescription);

    /**
     * 查询主题列表
     *
     * @param topicId          主题id
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 主题列表
     */
    List<Topic> queryTopics(String topicId, String topicDetail, String topicDescription);

}
