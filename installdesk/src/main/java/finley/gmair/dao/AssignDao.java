package finley.gmair.dao;

import finley.gmair.model.installation.Assign;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface AssignDao {

    ResultData insert(Assign assign);

    ResultData query(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition, int start, int length);

    ResultData principal(Map<String, Object> condition);

    ResultData worker(Map<String, Object> condition);

    ResultData update(Map<String, Object> condition);

    ResultData block(String assignId);

    ResultData remove(String assignId);
}
