package finley.gmair.controller;

import com.ctc.wstx.util.StringUtil;
import finley.gmair.form.installation.TeamForm;
import finley.gmair.model.installation.Team;
import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResultData createTeam(TeamForm form){
        ResultData result = new ResultData();

        String teamName = form.getTeamName().trim();
        String teamArea = form.getTeamArea().trim();
        String teamDescription = form.getTeamDescription().trim();

        //check whether input is empty
        if(StringUtils.isEmpty(form.getTeamName())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the Team name");
            return result;
        }
        if(StringUtils.isEmpty(form.getTeamArea())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the Team area");
            return result;
        }

        //check whether the team name exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamName", teamName);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("TeamController: Team: ").append(teamName).append(" already exist").toString());
            return result;
        }

        //create the the team
        Team team = new Team(teamName,teamArea,teamDescription);
        response = teamService.createTeam(team);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the team");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the team");
        return result;
    }

    @GetMapping("/list")
    public ResultData list(){
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFalg",false);
        ResultData response = teamService.fetchTeam(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to team info");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No team info at the moment");
        }
        else
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to team info");
        }
        return result;
    }

    @GetMapping("/update")
    public ResultData update(TeamForm team){
        ResultData result=new ResultData();
        //TODO
        return result;
    }
}
