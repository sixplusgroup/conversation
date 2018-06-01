package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineSettingDao {
    ResultData selectPowerSetting(Map<String, Object> condition);
}
