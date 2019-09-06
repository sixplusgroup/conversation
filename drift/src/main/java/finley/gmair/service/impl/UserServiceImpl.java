package finley.gmair.service.impl;

import finley.gmair.dao.UserDao;
import finley.gmair.dao.UserSessionDao;
import finley.gmair.model.drift.DriftUser;
import finley.gmair.model.wechat.UserSession;
import finley.gmair.service.UserService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName: UserServiceImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 4:58 PM
 */
@Service
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserSessionDao userSessionDao;

    @Autowired
    private UserDao userDao;

    @Override
    public ResultData createSession(UserSession session) {
        ResultData result = new ResultData();
        ResultData response = userSessionDao.replace(session);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建/更新用户Session失败,请稍后尝试");
        }
        return result;
    }

    @Override
    public ResultData fetchSession(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = userSessionDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能根据条件查找到相应的用户会话信息");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取用户的会话数据失败，请稍后尝试");
        }
        return result;
    }

    @Override
    public ResultData createUser(DriftUser user) {
        ResultData result = new ResultData();
        ResultData response = userDao.insert(user);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to create drift user into database");
        return result;
    }

    @Override
    public ResultData fetchUser(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = userDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No drift user found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve drift user from database");
        }
        return result;
    }

    @Override
    public ResultData updateUser(DriftUser user) {
        return userDao.update(user);
    }
}
