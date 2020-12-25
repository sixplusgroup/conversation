package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: ck
 * @date: 2020/11/2 13:51
 * @description:
 */
public interface TextDao {

    ResultData query(Map<String, Object> condition);

}
