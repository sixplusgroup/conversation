package finley.gmair.service.impl;

import finley.gmair.dao.MqttTopicDao;
import finley.gmair.model.mqttManagement.Topic;
import finley.gmair.service.TopicService;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 消息主题服务
 *
 * @author lycheeshell
 * @date 2020/12/15 16:13
 */
@Service
public class TopicServiceImpl implements TopicService {

    @Resource
    private MqttTopicDao mqttTopicDao;

    /**
     * 新增主题
     *
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 新增行数
     */
    @Override
    public int addTopic(String topicDetail, String topicDescription) {
        Topic topic = new Topic(topicDetail, topicDescription);
        topic.setTopicId(IDGenerator.generate("MTI"));
        return mqttTopicDao.insert(topic);
    }

    /**
     * 更新主题
     *
     * @param topicId          主题id
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 更新行数
     */
    @Override
    public int modifyTopic(String topicId, String topicDetail, String topicDescription) {
        Topic topic = new Topic(topicDetail, topicDescription);
        topic.setTopicId(topicId);
        return mqttTopicDao.update(topic);
    }

    /**
     * 查询主题列表
     *
     * @param topicId          主题id
     * @param topicDetail      主题格式
     * @param topicDescription 主题描述
     * @return 主题列表
     */
    @Override
    public List<Topic> queryTopics(String topicId, String topicDetail, String topicDescription) {
        Topic topic = new Topic(topicDetail, topicDescription);
        topic.setTopicId(topicId);
        return mqttTopicDao.query(topic);
    }
}
