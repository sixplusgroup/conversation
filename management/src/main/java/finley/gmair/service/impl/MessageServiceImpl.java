package finley.gmair.service.impl;

import finley.gmair.model.message.MessageTemplate;
import finley.gmair.service.MessageService;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {
    @Override
    public ResultData createTemplate(MessageTemplate template) {
        ResultData result = new ResultData();

        return result;
    }
}
