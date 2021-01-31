package finley.gmair.dao;

import finley.gmair.model.mqttManagement.Topic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 消息主题的数据操作
 *
 * @author lycheeshell
 * @date 2020/12/15 14:09
 */
@Mapper
public interface MqttTopicDao {

    /**
     * 新增主题
     *
     * @param topic 主题
     * @return 新增条数
     */
    int insert(Topic topic);

    /**
     * 删除主题
     *
     * @param topicId 主题id
     * @return 删除行数
     */
    int delete(String topicId);

    /**
     * 更新主题
     *
     * @param topic 更新信息
     * @return 更新条数
     */
    int update(Topic topic);

    /**
     * 查询主题列表
     *
     * @param topic 查询条件
     * @return 主题列表
     */
    List<Topic> query(Topic topic);

    /**
     * 模糊查询, 查询主题列表
     *
     * @param topicDetail 主题格式
     * @return 主题列表
     */
    List<Topic> queryTopicsByDetail(String topicDetail);
}
