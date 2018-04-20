package finley.gmair.service.impl;

import finley.gmair.dao.TeamDao;
import finley.gmair.model.installation.Team;
import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamDao teamDao;
    @Override
    public ResultData fetchTeam(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = teamDao.queryTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No team found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch team");
        }
        return result;
    }

    @Override
    public ResultData createTeam(Team team){
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("teamName",team.getTeamName());
        condition.put("blockFalg",false);

        ResultData response = teamDao.queryTeam(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("team with name: ").append(team.getTeamName()).append(" already exist").toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {

            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to finish the prerequisite check");
            System.out.println("in Service fetch fail "+team.getTeamName()+" "+team.getTeamArea()+" "+team.getDescription());
            return result;
        }

        response=teamDao.insertTeam(team);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert team" + team.toString());
            System.out.println("inService insert "+team.getTeamName()+" "+team.getTeamArea()+" "+team.getDescription());
        }
        return result;
    }


}
