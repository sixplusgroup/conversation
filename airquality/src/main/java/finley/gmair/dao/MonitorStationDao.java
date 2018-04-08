package finley.gmair.dao;

import finley.gmair.model.air.MonitorStation;
import finley.gmair.util.ResultData;

import java.util.List;

public interface MonitorStationDao {

    ResultData insert(MonitorStation monitorStation);
    ResultData insertBatch(List<MonitorStation> list);
}
