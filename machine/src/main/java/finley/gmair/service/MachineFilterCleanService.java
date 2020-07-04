package finley.gmair.service;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:31
 * @description: TODO
 */
public interface MachineFilterCleanService {

    ResultData fetch(Map<String, Object> condition);

    ResultData modify(Map<String, Object> condition);
}
