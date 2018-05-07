package finley.gmair.dao;

import finley.gmair.model.installation.Team;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TeamDao {
    ResultData insertTeam(Team team);

    ResultData queryTeam(Map<String, Object> condition);

    ResultData updateTeam(Team team);
}
