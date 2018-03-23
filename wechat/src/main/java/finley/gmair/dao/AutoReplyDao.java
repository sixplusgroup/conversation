package finley.gmair.dao;

import finley.gmair.model.wechat.AutoReply;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AutoReplyDao {
    ResultData insert(AutoReply autoReply);

    ResultData query(Map<String, Object> condition);

    ResultData update(AutoReply autoReply);
}
