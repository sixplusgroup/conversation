package finley.gmair.service.Impl;

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

    @Override
    public ResultData insertBatchHourly(List<HeatHourly> list) {
        return heatHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return heatHourlyDao.query(condition);
    }


}
