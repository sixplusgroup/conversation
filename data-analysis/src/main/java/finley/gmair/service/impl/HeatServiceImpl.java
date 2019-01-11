package finley.gmair.service.impl;

import finley.gmair.dao.HeatDailyDao;
import finley.gmair.dao.HeatHourlyDao;
import finley.gmair.model.dataAnalysis.HeatHourly;
import finley.gmair.service.HeatService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HeatServiceImpl implements HeatService {

    @Autowired
    private HeatHourlyDao heatHourlyDao;

    @Autowired
    private HeatDailyDao heatDailyDao;

    @Override
    public ResultData insertBatchHourly(List<HeatHourly> list) {
        return heatHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return heatHourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<HeatHourly> list) { return heatDailyDao.insertBatch(list); }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) {
        return heatDailyDao.query(condition);
    }


}
