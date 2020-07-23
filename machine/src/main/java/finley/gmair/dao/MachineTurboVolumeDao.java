package finley.gmair.dao;

import finley.gmair.model.machine.MachineTurboVolume;
import finley.gmair.util.ResultData;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:13
 * @description: TODO
 */
public interface MachineTurboVolumeDao {

    ResultData add(MachineTurboVolume machineTurboVolume);

    ResultData update(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition);
}
