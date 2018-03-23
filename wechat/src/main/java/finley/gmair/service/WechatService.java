package finley.gmair.service;

import finley.gmair.model.wechat.AccessToken;
import finley.gmair.util.ResultData;

public interface WechatService {
    ResultData refreshAccessToken(AccessToken token);
}
