package finley.gmair.dao;

import finley.gmair.model.analysis.ComponentMean;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface ComponentMeanDao {

    ResultData query(Map<String, Object> condition);

    ResultData insertBatch(List<ComponentMean> list);
}
