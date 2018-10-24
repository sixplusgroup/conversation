package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface FilterLimitConfigService {

    ResultData fetch(Map<String, Object> condition);

}
