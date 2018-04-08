package finley.gmair.dao;

import finley.gmair.model.air.AirQuality;
import finley.gmair.util.ResultData;

import java.util.List;

public interface AirQualityDao {

    ResultData insert(AirQuality airQuality);
    ResultData insertLatest(AirQuality airQuality);
    ResultData insertBatch(List<AirQuality> list);
    ResultData insertLatestBatch(List<AirQuality> list);
}
