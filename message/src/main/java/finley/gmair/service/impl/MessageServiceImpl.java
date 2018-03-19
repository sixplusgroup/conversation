package finley.gmair.service.impl;

import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public ResultData createTextMessage(TextMessage message) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData fetchTextMessage(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }
}
