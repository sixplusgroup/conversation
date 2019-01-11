package finley.gmair.service.Impl;

import finley.gmair.dao.IndoorPm25DailyDao;
import finley.gmair.dao.IndoorPm25HourlyDao;
import finley.gmair.model.dataAnalysis.IndoorPm25Hourly;
import finley.gmair.service.IndoorPm25Service;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndoorPm25ServiceImpl implements IndoorPm25Service {

    @Autowired
    private IndoorPm25HourlyDao indoorPm25HourlyDao;

    @Autowired
    private IndoorPm25DailyDao indoorPm25DailyDao;

    @Override
    public ResultData insertBatchHourly(List<IndoorPm25Hourly> list) {
        return indoorPm25HourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return indoorPm25HourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<IndoorPm25Hourly> list) { return indoorPm25DailyDao.insertBatch(list); }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) { return indoorPm25DailyDao.query(condition); }

}
