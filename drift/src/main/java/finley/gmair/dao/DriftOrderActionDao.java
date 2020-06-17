package finley.gmair.dao;

import finley.gmair.model.drift.DriftOrderAction;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftOrderActionDao {

    ResultData query(Map<String, Object> condition);

    ResultData insert(DriftOrderAction driftOrderAction);
}