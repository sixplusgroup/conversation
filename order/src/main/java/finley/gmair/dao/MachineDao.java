package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.util.Map;

public interface MachineDao {
    ResultData queryInstallType(Map<String, Object> condition);
}
