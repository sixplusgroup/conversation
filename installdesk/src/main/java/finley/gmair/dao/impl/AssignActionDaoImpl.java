package finley.gmair.dao.impl;

import finley.gmair.dao.AssignActionDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.installation.AssignAction;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: AssignActionDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/15 2:44 PM
 */
@Repository
public class AssignActionDaoImpl extends BaseDao implements AssignActionDao {
    private Logger logger = LoggerFactory.getLogger(AssignActionDaoImpl.class);

    @Override
    public ResultData insert(AssignAction action) {
        ResultData result = new ResultData();
        action.setActionId(IDGenerator.generate("AAN"));
        try {
            sqlSession.insert("gmair.install.assign.action.insert", action);
            result.setData(action);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.assign.action.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {

        }
        return result;
    }
}
