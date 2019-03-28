package finley.gmair.dao;

import finley.gmair.model.installation.Team;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TeamDao {
    ResultData insert(Team team);

    ResultData query(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition, int start, int length);

    ResultData update(Map<String, Object> condition);

    ResultData block(String teamId);

    ResultData remove(String teamId);
}
