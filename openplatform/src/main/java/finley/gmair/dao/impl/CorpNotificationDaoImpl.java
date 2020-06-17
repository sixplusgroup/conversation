package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.CorpNotificationDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: CorpNotificationDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/11/13 2:11 PM
 */
@Repository
public class CorpNotificationDaoImpl extends BaseDao implements CorpNotificationDao {
    private Logger logger = LoggerFactory.getLogger(CorpNotificationDaoImpl.class);

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.openplatform.corp.notification.query", condition);
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
