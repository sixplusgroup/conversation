package finley.gmair.service;

import finley.gmair.model.wechat.WechatResource;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface WechatResourceService {
    ResultData create(WechatResource resource);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(WechatResource resource);

    boolean existResource(Map<String, Object> condition);
}
