package finley.gmair.dao;

import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResultData;

public interface MessageTemplateDao {
    ResultData insertTemplate(MessageTemplate template);
}
