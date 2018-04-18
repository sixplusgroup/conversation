package finley.gmair.dao;

import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TextTemplateDao {
    ResultData insert(TextTemplate textTemplate);

    ResultData query(Map<String, Object> condition);

    ResultData update(TextTemplate textTemplate);

    ResultData queryTextReply(Map<String, Object> condition);
}
