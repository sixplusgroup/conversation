package finley.gmair.service.Impl;

import finley.gmair.dao.ModeHourlyDao;
import finley.gmair.dao.PowerHourlyDao;
import finley.gmair.model.dataAnalysis.ModeHourly;
import finley.gmair.model.dataAnalysis.PowerHourly;
import finley.gmair.service.ModeService;
import finley.gmair.service.PowerService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModeServiceImpl implements ModeService {

    @Autowired
    private ModeHourlyDao modeHourlyDao;

    @Override
    public ResultData insertBatchHourly(List<ModeHourly> list) {
        return modeHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return modeHourlyDao.query(condition);
    }


}
