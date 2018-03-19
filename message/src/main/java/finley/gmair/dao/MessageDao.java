package finley.gmair.dao;

import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageDao {
    ResultData insert(TextMessage message);

    ResultData query(Map<String, Object> condition);
}
