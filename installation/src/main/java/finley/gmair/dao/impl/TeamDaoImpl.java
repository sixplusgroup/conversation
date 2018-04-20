package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TeamDao;
import finley.gmair.model.installation.Team;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TeamDaoImpl extends BaseDao implements TeamDao {
    @Override
    public ResultData insertTeam(Team team) {
        ResultData result = new ResultData();
        team.setTeamId(IDGenerator.generate("ITM"));
        try{
            sqlSession.insert("gmail.installation.team.insert",team);
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
        try{
            List<Team> list=sqlSession.selectList("gmail.installation.team.query",condition);
            result.setData(list);
        }
        catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateTeam(Team team){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmail.installation.team.update",team);
            result.setData(team);
        }catch(Exception e){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
