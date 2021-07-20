package finley.gmair.service;

import finley.gmair.model.analysis.ModeHourly;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ModeService {

    ResultData insertBatchHourly(List<ModeHourly> list);

    ResultData fetchHourly(Map<String, Object> condition);

    ResultData insertBatchDaily(List<ModeHourly> list);

    ResultData fetchDaily(Map<String, Object> condition);
}
