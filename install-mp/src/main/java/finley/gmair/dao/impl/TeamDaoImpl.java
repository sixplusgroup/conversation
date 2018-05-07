package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TeamDao;
import finley.gmair.model.installation.Team;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TeamDaoImpl extends BaseDao implements TeamDao {
    @Override
    public ResultData insertTeam(Team team) {
        ResultData result = new ResultData();
        team.setTeamId(IDGenerator.generate("ITM"));
        try{
            sqlSession.insert("gmair.installation.team.insert",team);
            result.setData(team);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryTeam(Map<String, Object> condition){
        ResultData result = new ResultData();
        List<Team> list=new ArrayList<>();
        try{
            list=sqlSession.selectList("gmair.installation.team.query",condition);
            result.setData(list);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }

        if(result.getResponseCode()!=ResponseCode.RESPONSE_ERROR) {
            if (list.isEmpty() == true) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No team found");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("Success to found team");
            }
        }
        return result;
    }

    @Override
    public ResultData updateTeam(Team team){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.installation.team.update",team);
            result.setData(team);
        }catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
