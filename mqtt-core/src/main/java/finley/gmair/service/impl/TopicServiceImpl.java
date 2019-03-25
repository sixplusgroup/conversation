package finley.gmair.service.impl;

import finley.gmair.dao.TopicDao;
import finley.gmair.model.mqtt.ApiTopic;
import finley.gmair.service.TopicService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return topicDao.query(condition);
    }

    @Override
    public ResultData create(ApiTopic topic) {
        return topicDao.insert(topic);
    }

    @Override
    public ResultData modify(Map<String, Object> condition) {
        return topicDao.update(condition);
    }
}
