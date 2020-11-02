package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author ：CK
 * @date ：Created in 2020/11/2 11:33
 * @description：
 */
public interface TextService {

    ResultData fetch(Map<String, Object> condition);

}
