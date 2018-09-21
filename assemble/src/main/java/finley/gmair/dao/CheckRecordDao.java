package finley.gmair.dao;

import finley.gmair.model.assemble.CheckRecord;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CheckRecordDao {
    ResultData insert(CheckRecord checkRecord);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
