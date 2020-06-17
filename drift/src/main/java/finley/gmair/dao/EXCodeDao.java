package finley.gmair.dao;

import finley.gmair.model.drift.EXCode;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface EXCodeDao {

    ResultData insert(EXCode code);

    ResultData query(Map<String, Object> condition);

    ResultData queryLabel(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
