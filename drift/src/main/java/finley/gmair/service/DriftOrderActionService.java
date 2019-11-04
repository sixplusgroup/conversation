package finley.gmair.service;

import finley.gmair.model.drift.DriftOrderAction;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftOrderActionService {

    ResultData create(DriftOrderAction driftOrderAction);

    ResultData fetch(Map<String, Object> condition);
}
