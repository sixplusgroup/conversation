package finley.gmair.dao;

import finley.gmair.model.wechat.AccessToken;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AccessTokenDao {
    ResultData insert(AccessToken token);

    ResultData query(Map<String, Object> condition);

    ResultData update(AccessToken token);
}
