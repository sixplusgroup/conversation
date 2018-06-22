package finley.gmair.dao;

import finley.gmair.model.machine.ControlOptionAction;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
public interface ControlOptionActionDao {
    ResultData insert(ControlOptionAction action);

    ResultData query(Map<String, Object> condition);
}
