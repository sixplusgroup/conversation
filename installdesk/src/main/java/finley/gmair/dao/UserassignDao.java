package finley.gmair.dao;

import finley.gmair.model.installation.Userassign;
import finley.gmair.util.ResultData;
import org.apache.catalina.User;

import java.util.Map;

public interface UserassignDao {
    ResultData insert(Userassign userassign);

    ResultData query(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition,int start, int length);

    ResultData principal(Map<String, Object> condition);

    ResultData principal(Map<String, Object> condition,int start,int length);

    ResultData updateUserassignStatus(String userassignId,int status);

    ResultData update(Userassign userassign);
}
