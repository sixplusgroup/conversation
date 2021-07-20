package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 15:59 2019/8/16
 */
public interface VerificationService {

    ResultData verify(String openid, String idCard, String name);

    ResultData fetch(Map<String, Object> condition);

}
