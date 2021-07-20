package finley.gmair.dao;

import finley.gmair.model.drift.DriftReport;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DriftReportDao {

    ResultData query(Map<String,Object> condition);

    ResultData insert(DriftReport driftReport);

}
