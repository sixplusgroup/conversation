package finley.gmair.dao.impl;

import finley.gmair.dao.AccessTokenDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Repository
public class AccessTokenDaoImpl extends BaseDao implements AccessTokenDao {
    private Logger logger = LoggerFactory.getLogger(AccessTokenDaoImpl.class);

    @Transactional
    @Override
    public ResultData insert(AccessToken token) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.wechat.accesstoken.insert", token);
            result.setData(token);
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
            AccessToken token = sqlSession.selectOne("gmair.wechat.accesstoken.query", condition);
            if (StringUtils.isEmpty(token)) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(AccessToken token) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.accesstoken.update", token);
            result.setData(token);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
