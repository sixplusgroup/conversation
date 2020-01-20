package finley.gmair.service;

import com.mysql.cj.protocol.Message;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.MessageTemplate;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface MessageTemplateService {
    ResultData create(MessageTemplate token);

    ResultData fetch(Map<String, Object> condition);

    ResultData renew(MessageTemplate token);
}
