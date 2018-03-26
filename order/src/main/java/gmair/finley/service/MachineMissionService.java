package gmair.finley.service;


import finley.gmair.model.order.MachineMission;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * Created by hushe on 2018/1/17.
 */
public interface MachineMissionService {
    ResultData fetch(Map<String, Object> condition);

    ResultData create(MachineMission machineMission);

}
