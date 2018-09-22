package finley.gmair.dao.impl;

import finley.gmair.dao.ActivityDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.drift.Activity;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ActivityDaoImpl extends BaseDao implements ActivityDao {
    @Override
    public ResultData queryActivity(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData insertActivity(Activity activity) {
        return null;
    }

    @Override
    public ResultData updateActivity(Activity activity) {
        return null;
    }
}
