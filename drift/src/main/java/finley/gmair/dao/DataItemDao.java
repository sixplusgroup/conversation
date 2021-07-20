package finley.gmair.dao;

import finley.gmair.model.drift.DataItem;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface DataItemDao {

    ResultData query(Map<String,Object> condition);

    ResultData insert(DataItem dataItem);
}
