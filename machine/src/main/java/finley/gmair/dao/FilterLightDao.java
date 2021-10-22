package finley.gmair.dao;

import finley.gmair.model.machine.FilterLight;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FilterLightDao {
    ResultData insert(FilterLight filterLight);

    ResultData query(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

}
