package finley.gmair.service.Impl;

import finley.gmair.dao.UserActionDailyDao;
import finley.gmair.model.dataAnalysis.UserActionDaily;
import finley.gmair.service.UserActionDailyService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserActionDailyServiceImpl implements UserActionDailyService {

    @Autowired
    private UserActionDailyDao userActionDailyDao;

    @Override
    public ResultData insertBatchDaily(List<UserActionDaily> list){return userActionDailyDao.insertBatch(list);}

    @Override
    public ResultData fetchDaily(Map<String, Object> condition){return userActionDailyDao.query(condition);}
}
