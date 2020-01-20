package finley.gmair.service;

import finley.gmair.model.drift.EXCode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface EXCodeService {

    ResultData createEXCode(String activityId, int status, int num, double price);

    ResultData createOneExcode(EXCode excode);

    ResultData fetchEXCode(Map<String, Object> condition);

    ResultData modifyEXCode(Map<String, Object> condition);
}
