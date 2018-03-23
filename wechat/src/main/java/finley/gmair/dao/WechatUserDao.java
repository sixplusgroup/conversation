package finley.gmair.dao;

import finley.gmair.model.wechat.WechatUser;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface WechatUserDao {
    ResultData insert(WechatUser wechatUser);

    ResultData query(Map<String, Object> condition);

    ResultData update(WechatUser wechatUser);
}
