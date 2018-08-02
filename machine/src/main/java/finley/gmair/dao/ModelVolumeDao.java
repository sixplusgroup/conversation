package finley.gmair.dao;

import finley.gmair.model.machine.BoundaryPM2_5;
import finley.gmair.model.machine.ModelVolume;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ModelVolumeDao {
    ResultData insert(ModelVolume modelVolume);

    ResultData query(Map<String, Object> condition);

    ResultData updateByModelId(Map<String, Object> condition);
}