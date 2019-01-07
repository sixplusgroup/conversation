package finley.gmair.service.Impl;

import finley.gmair.dao.HumidHourlyDao;
import finley.gmair.model.dataAnalysis.HumidHourly;
import finley.gmair.service.HumidService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HumidServiceImpl implements HumidService {

    @Autowired
    private HumidHourlyDao humidHourlyDao;

    @Override
    public ResultData insertBatchHourly(List<HumidHourly> list) {
        return humidHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return humidHourlyDao.query(condition);
    }


}
