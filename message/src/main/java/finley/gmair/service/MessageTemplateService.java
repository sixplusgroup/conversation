package finley.gmair.service;

import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageTemplateService {
    ResultData createTemplate(MessageTemplate template);

    ResultData fetchTemplate(Map<String, Object> condition);

    ResultData reviseTemplate(MessageTemplate template);
}
