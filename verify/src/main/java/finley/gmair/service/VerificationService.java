package finley.gmair.service;

import finley.gmair.util.ResultData;

/**
 * @Author ：lycheeshell
 * @Date ：Created in 15:59 2019/8/16
 */
public interface VerificationService {

    ResultData verify(String idCard, String name);

}
