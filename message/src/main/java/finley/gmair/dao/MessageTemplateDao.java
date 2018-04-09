package finley.gmair.dao;

import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageTemplateDao {
    ResultData insertMessageTemplate(MessageTemplate template);

    ResultData queryMessageTemplate(Map<String, Object> condition);

    ResultData updateMessageTemplate(MessageTemplate template);
}
