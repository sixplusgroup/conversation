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
    Topic addTopic(String topicDetail, String topicDescription);

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     */
    int deleteTopic(String topicId);

    /**
     * 更新主题
     * 设计已运行时的订阅主题，删除主题并新增代替本方法
     *
     * @param topicId          主题id
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 更新行数
     */
    @Deprecated
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

    /**
     * 模糊查询, 查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    List<Topic> queryTopicsByDetail(String topicDetail);

}
