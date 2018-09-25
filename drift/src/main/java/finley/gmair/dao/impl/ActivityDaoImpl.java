package finley.gmair.dao.impl;

import finley.gmair.dao.ActivityDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.drift.Activity;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ActivityDaoImpl extends BaseDao implements ActivityDao {
    @Override
    public ResultData queryActivity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<Activity> list = sqlSession.selectList("gmair.drift.activity.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertActivity(Activity activity) {
        ResultData result = new ResultData();
        activity.setActivityId(IDGenerator.generate("ACT"));
        try {
            sqlSession.insert("gmair.drift.activity.insert", activity);
            result.setData(activity);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateActivity(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.activity.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
