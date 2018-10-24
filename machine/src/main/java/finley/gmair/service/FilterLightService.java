package finley.gmair.service;

import finley.gmair.model.machine.FilterLight;
import finley.gmair.model.machine.OutPm25Daily;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface FilterLightService {
    ResultData create(FilterLight filterLight);

    ResultData fetch(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);
}
