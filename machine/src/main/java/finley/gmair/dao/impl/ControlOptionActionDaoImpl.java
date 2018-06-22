package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ControlOptionActionDao;
import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
@Repository
public class ControlOptionActionDaoImpl extends BaseDao implements ControlOptionActionDao {
    @Override
    public ResultData insert(ControlOptionAction action) {
        ResultData result = new ResultData();
        action.setValueId(IDGenerator.generate("COA"));
        try {
            sqlSession.insert("gmair.machine.control.option.insertAction", action);
            result.setData(action);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ControlOptionAction> list = sqlSession.selectList("gmair.machine.control.option.queryAction", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}