package finley.gmair.dao;

import finley.gmair.model.analysis.VolumeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface VolumeHourlyDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<VolumeHourly> list);

}
