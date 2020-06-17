package finley.gmair.service;

import finley.gmair.model.drift.DriftOrderCancel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftOrderCancelService {

    ResultData createCancel(DriftOrderCancel driftOrderCancel);

    ResultData fetchCancel(Map<String, Object> condition);

    ResultData updateCancel(DriftOrderCancel driftOrderCancel);
}
