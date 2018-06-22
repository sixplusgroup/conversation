package finley.gmair.dao;

import finley.gmair.model.machine.ControlOption;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author shenghaohu@hshvip.com
 * @date 2018/6/22
 */
public interface ControlOptionDao {
    ResultData insert(ControlOption option);

    ResultData query(Map<String, Object> condition);
}
