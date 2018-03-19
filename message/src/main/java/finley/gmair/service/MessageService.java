package finley.gmair.service;

import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageService {
    ResultData createTextMessage(TextMessage message);

    ResultData fetchTextMessage(Map<String, Object> condition);
}
