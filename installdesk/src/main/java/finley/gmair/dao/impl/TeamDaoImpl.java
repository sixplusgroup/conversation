package finley.gmair.dao.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TeamDao;
import finley.gmair.model.installation.Team;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TeamDaoImpl extends BaseDao implements TeamDao {
    private Logger logger = LoggerFactory.getLogger(TeamDaoImpl.class);

    @Override
    public ResultData insert(Team team) {
        ResultData result = new ResultData();
        team.setTeamId(IDGenerator.generate("ITM"));
        try {
            sqlSession.insert("gmair.install.team.insert", team);
            result.setData(team);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.team.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition, int start, int length) {
        ResultData result = new ResultData();
        JSONObject data = new JSONObject();
        try {
            List list = sqlSession.selectList("gmair.install.team.query", condition);
            data.put("size", list.size());
            list = sqlSession.selectList("gmair.install.team.query", condition, new RowBounds(start, length));
            data.put("data", list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.team.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData block(String teamId) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.team.block", teamId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData remove(String teamId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.install.team.remove", teamId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
