package finley.gmair.dao.impl;

import finley.gmair.dao.AssignCodeDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.installation.AssignCode;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AssignCodeDaoImpl
 * @Description: TODO
 * @Author jiaqi wang
 * @Date 2019/4/15 2:44 PM
 */
@Repository
public class AssignCodeDaoImpl extends BaseDao implements AssignCodeDao {
    private Logger logger = LoggerFactory.getLogger(AssignCodeDaoImpl.class);

    @Override
    public ResultData insert(AssignCode assignCode) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.install.assign.code.insert", assignCode);
            result.setData(assignCode);
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
            List list = sqlSession.selectList("gmair.install.assign.code.query", condition);
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
