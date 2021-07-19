package finley.gmair.dao;

import finley.gmair.model.membership.IntegralAdd;
import finley.gmair.util.ResultData;

public interface IntegralDao{
     ResultData insert(IntegralAdd integralAdd);
     ResultData getOneById(String addId);
     ResultData updateConfirm(String addId);
}
