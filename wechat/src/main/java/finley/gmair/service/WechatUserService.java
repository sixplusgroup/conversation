package finley.gmair.service;

import finley.gmair.model.wechat.WechatUser;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface WechatUserService {
    ResultData create(WechatUser user);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(WechatUser user);

    boolean existWechatUser(Map<String, Object> condition);
}
