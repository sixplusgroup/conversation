package finley.gmair.service.impl;

import finley.gmair.dao.MqttTopicDao;
import finley.gmair.model.mqttManagement.Topic;
import finley.gmair.mqtt.MqttConfiguration;
import finley.gmair.service.TopicService;
import finley.gmair.util.IDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(TopicServiceImpl.class);

    @Resource
    private MqttConfiguration mqttConfiguration;

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
    public Topic addTopic(String topicDetail, String topicDescription) {
        List<Topic> existTopics = queryTopics(null, topicDetail, null);
        if (existTopics != null && existTopics.size() > 0) {
            return null;
        }

        Topic topic = new Topic(topicDetail, topicDescription);
        topic.setTopicId(IDGenerator.generate("MTI"));

        // 1.数据库新增主题
        mqttTopicDao.insert(topic);

        // 2.运行中动态新增订阅的主题
        mqttConfiguration.addInboundTopic(new String[]{topicDetail});

        return topic;
    }

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     */
    @Override
    public int deleteTopic(String topicId) {
        Topic queryTopic = new Topic();
        queryTopic.setTopicId(topicId);
        List<Topic> topicResult = mqttTopicDao.query(queryTopic);
        if (topicResult == null || topicResult.size() == 0) {
            logger.warn("database do not exist topicId " + topicId);
            return 0;
        }
        String topicDetail = topicResult.get(0).getTopicDetail();

        // 1.数据库删除主题
        int line = mqttTopicDao.delete(topicId);

        // 2.运行中动态删除订阅的主题
        mqttConfiguration.removeInboundTopic(new String[]{topicDetail});

        return line;
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
        Topic queryTopic = new Topic(topicDetail, topicDescription);
        queryTopic.setTopicId(topicId);
        return mqttTopicDao.query(queryTopic);
    }

    /**
     * 模糊查询, 查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    @Override
    public List<Topic> queryTopicsByDetail(String topicDetail) {
        return mqttTopicDao.queryTopicsByDetail(topicDetail);
    }
}
