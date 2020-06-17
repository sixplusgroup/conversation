package finley.gmair.service.impl;

import finley.gmair.dao.TempDailyDao;
import finley.gmair.dao.TempHourlyDao;
import finley.gmair.model.analysis.TempHourly;
import finley.gmair.service.TempService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TempServiceImpl implements TempService {

    @Autowired
    private TempHourlyDao tempHourlyDao;

    @Autowired
    private TempDailyDao tempDailyDao;

    @Override
    public ResultData insertBatchHourly(List<TempHourly> list) {
        return tempHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return tempHourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<TempHourly> list) {
        return tempDailyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) {
        return tempDailyDao.query(condition);
    }

}
