package finley.gmair.service;

import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResultData;

public interface MessageService {
    ResultData createTemplate(MessageTemplate template);
}
