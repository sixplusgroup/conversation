package finley.gmair.dao;

import finley.gmair.model.drift.VerifyInfo;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 13:53 2019/8/22
 */
public interface VerificationDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(VerifyInfo verifyInfo);
}
