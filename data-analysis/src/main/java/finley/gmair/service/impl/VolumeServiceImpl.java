package finley.gmair.service.impl;

import finley.gmair.dao.VolumeDailyDao;
import finley.gmair.dao.VolumeHourlyDao;
import finley.gmair.model.dataAnalysis.VolumeHourly;
import finley.gmair.service.VolumeService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VolumeServiceImpl implements VolumeService {

    @Autowired
    private VolumeHourlyDao volumeHourlyDao;

    @Autowired
    private VolumeDailyDao volumeDailyDao;

    @Override
    public ResultData insertBatchHourly(List<VolumeHourly> list) {
        return volumeHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return volumeHourlyDao.query(condition);
    }

    @Override
    public ResultData insertBatchDaily(List<VolumeHourly> list) {
        return volumeDailyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchDaily(Map<String, Object> condition) {
        return volumeDailyDao.query(condition);
    }

}
