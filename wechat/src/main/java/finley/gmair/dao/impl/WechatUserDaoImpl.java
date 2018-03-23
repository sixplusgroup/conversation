package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.WechatUserDao;
import finley.gmair.model.wechat.WechatUser;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class WechatUserDaoImpl extends BaseDao implements WechatUserDao {
    private Logger logger = LoggerFactory.getLogger(WechatUserDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(WechatUser wechatUser) {
        ResultData result = new ResultData();
        wechatUser.setUserId(IDGenerator.generate("WCU"));
        try {
            sqlSession.insert("gmair.wechat.user.insert", wechatUser);
            result.setData(wechatUser);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<WechatUser> list = sqlSession.selectList("gmair.wechat.user.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(WechatUser wechatUser) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.user.update", wechatUser);
            result.setData(wechatUser);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}