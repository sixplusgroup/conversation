package finley.gmair.service;

import finley.gmair.model.analysis.VolumeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

/**
 * 查询设备风量接口
 */
public interface VolumeService {

    ResultData insertBatchHourly(List<VolumeHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<VolumeHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
