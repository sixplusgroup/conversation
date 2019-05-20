package finley.gmair.dao;

import finley.gmair.model.installation.AssignCode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignCodeDao {
    ResultData insert(AssignCode assignCode);

    ResultData query(Map<String, Object> condition);
}
