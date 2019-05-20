package finley.gmair.service;

import finley.gmair.model.installation.Team;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TeamService {
    ResultData create(Team team);

    ResultData fetch(Map<String, Object> condition);

    ResultData fetch(Map<String, Object> condition, int start, int length);

    ResultData update(Map<String, Object> condition);

    ResultData block(String teamId);

    ResultData remove(String teamId);
}
