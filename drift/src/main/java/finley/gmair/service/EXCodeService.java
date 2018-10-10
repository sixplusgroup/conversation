package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface EXCodeService {

    ResultData createEXCode(String activityId, int num);

    ResultData fetchEXCode(Map<String, Object> condition);

    ResultData modifyEXCode(Map<String, Object> condition);
}
