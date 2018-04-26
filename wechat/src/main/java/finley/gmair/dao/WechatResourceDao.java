package finley.gmair.dao;

import finley.gmair.model.wechat.WechatResource;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface WechatResourceDao{
    ResultData insert(WechatResource resource);

    ResultData query(Map<String, Object> condition);

    ResultData update(WechatResource resource);
}
