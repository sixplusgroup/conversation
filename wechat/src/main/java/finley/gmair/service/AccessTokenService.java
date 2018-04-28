package finley.gmair.service;

import finley.gmair.model.wechat.AccessToken;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AccessTokenService {
    ResultData create(AccessToken token);

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(AccessToken token);

    boolean existToken(Map<String, Object> condition);
}
