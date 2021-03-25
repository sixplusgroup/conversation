package finley.gmair.service;

import finley.gmair.model.wechat.ReplyRecord;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ReplyRecordService {
    ResultData create(ReplyRecord record);

    ResultData fetch(Map<String, Object> condition);
}
