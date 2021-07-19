package finley.gmair.service;

import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.util.ResultData;

public interface IntegralService {
    ResultData addIntegral(IntegralAdd integralAdd);
    boolean checkIntegralIsValid(String addId);
    ResultData confirmIntegral(String addId,String consumerId);
}
