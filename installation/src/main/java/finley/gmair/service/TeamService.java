package finley.gmair.service;

import finley.gmair.model.installation.Team;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TeamService {
    ResultData createTeam(Team team);

    ResultData fetchTeam(Map<String, Object> condition);
}
