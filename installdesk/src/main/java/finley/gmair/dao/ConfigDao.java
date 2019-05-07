package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @ClassName: ConfigDao
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/26 4:00 PM
 */
public interface ConfigDao {
    ResultData query(Map<String, Object> condition);
}
