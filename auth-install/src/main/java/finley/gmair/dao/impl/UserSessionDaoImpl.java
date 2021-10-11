package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.UserSessionDao;
import finley.gmair.model.wechat.UserSession;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserSessionDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 4:37 PM
 */
@Repository
public class UserSessionDaoImpl extends BaseDao implements UserSessionDao {
    private Logger logger = LoggerFactory.getLogger(UserSessionDaoImpl.class);

    @Override
    public ResultData replace(UserSession session) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.user.session.insert", session);
            result.setData(session);
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
            List list = sqlSession.selectList("gmair.user.session.query", condition);
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
}
