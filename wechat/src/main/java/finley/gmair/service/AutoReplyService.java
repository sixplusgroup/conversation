package finley.gmair.service;

import finley.gmair.model.wechat.AutoReply;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AutoReplyService {
    ResultData create(AutoReply reply);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(AutoReply reply);
}
