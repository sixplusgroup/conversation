package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface ConfigService {
    ResultData fetch(Map<String, Object> condition);
}
