package finley.gmair.service.impl;

import finley.gmair.dao.PowerDailyDao;
import finley.gmair.dao.PowerHourlyDao;
import finley.gmair.model.dataAnalysis.PowerHourly;
import finley.gmair.service.PowerService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PowerServiceImpl implements PowerService {

    @Autowired
    private PowerHourlyDao powerHourlyDao;

    @Autowired
    private PowerDailyDao powerDailyDao;

    @Override
    public ResultData insertBatchHourly(List<PowerHourly> list) {
        return powerHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return powerHourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<PowerHourly> list) { return powerDailyDao.insertBatch(list); }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) { return powerDailyDao.query(condition); }
}
