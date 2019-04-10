package finley.gmair.service;

import finley.gmair.model.wechat.UserSession;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: UserService
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 4:57 PM
 */
public interface UserService {
    ResultData createSession(UserSession session);

    ResultData fetchSession(Map<String, Object> condition);
}
