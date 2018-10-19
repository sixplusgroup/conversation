package finley.gmair.dao;

import finley.gmair.model.formaldehyde.CaseLngLat;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface CaseLngLatDao {

    ResultData insert(CaseLngLat caseLngLat);

    ResultData query(Map<String, Object> condition);

}
