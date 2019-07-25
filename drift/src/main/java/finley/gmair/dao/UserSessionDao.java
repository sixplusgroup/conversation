package finley.gmair.dao;

import finley.gmair.model.wechat.UserSession;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: UserDao
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 4:36 PM
 */
public interface UserSessionDao {
    ResultData replace(UserSession session);

    ResultData query(Map<String, Object> condition);
}
