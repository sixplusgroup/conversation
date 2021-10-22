package finley.gmair.dao;

import finley.gmair.model.wechat.MessageTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageTemplateDao {
    ResultData insert(MessageTemplate token);

    ResultData query(Map<String, Object> condition);

    ResultData update(MessageTemplate token);
}