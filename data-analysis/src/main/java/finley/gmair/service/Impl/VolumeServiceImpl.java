package finley.gmair.service.Impl;

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

    @Override
    public ResultData insertBatchHourly(List<VolumeHourly> list) {
        return volumeHourlyDao.insertBatch(list);
    }

    @Override
    public ResultData fetchHourly(Map<String, Object> condition) {
        return volumeHourlyDao.query(condition);
    }


}
