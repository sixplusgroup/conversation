package finley.gmair.service.impl;

import finley.gmair.dao.Co2DailyDao;
import finley.gmair.dao.Co2HourlyDao;
import finley.gmair.model.analysis.Co2Hourly;
import finley.gmair.service.Co2Service;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Co2ServiceImpl implements Co2Service {

    @Autowired
    private Co2HourlyDao co2HourlyDao;

    @Autowired
    private Co2DailyDao co2DailyDao;

    @Override
    public ResultData insertBatchHourly(List<Co2Hourly> list) {
        return co2HourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return co2HourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<Co2Hourly> list) {
        return co2DailyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) { return co2DailyDao.query(condition); }


}
