package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.UserActionDailyDao;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserActionDailyDaoImpl extends BaseDao implements UserActionDailyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<UserActionDaily> list = sqlSession.selectList("gmair.dataAnalysis.user_action.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<UserActionDaily> list) {
        ResultData result = new ResultData();
        for (UserActionDaily uad: list)
            if (uad.getRecordId() == null)
                uad.setRecordId(IDGenerator.generate("UserAction"));
        try {
            sqlSession.insert("gmair.machine.user_action.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
