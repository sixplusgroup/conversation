package finley.gmair.dao;

import finley.gmair.model.drift.DriftOrderCancel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftOrderCancelDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(DriftOrderCancel driftOrderCancel);

    ResultData update(DriftOrderCancel driftOrderCancel);
}
