package finley.gmair.service;

import finley.gmair.model.dataAnalysis.VolumeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface VolumeService {

    ResultData insertBatchHourly(List<VolumeHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

}
