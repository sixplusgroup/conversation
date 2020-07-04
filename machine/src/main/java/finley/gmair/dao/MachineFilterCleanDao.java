package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:51
 * @description: TODO
 */
public interface MachineFilterCleanDao {

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
