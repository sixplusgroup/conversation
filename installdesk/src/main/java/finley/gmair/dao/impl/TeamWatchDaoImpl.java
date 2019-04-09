package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TeamWatchDao;
import finley.gmair.model.installation.TeamWatch;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: TeamWatchDaoImpl
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/3 3:47 PM
 */
@Repository
public class TeamWatchDaoImpl extends BaseDao implements TeamWatchDao {
    private Logger logger = LoggerFactory.getLogger(TeamWatchDaoImpl.class);

    @Override
    public ResultData insert(TeamWatch tw) {
        ResultData result = new ResultData();
        tw.setWatchId(IDGenerator.generate("TWH"));
        try {
            sqlSession.insert("gmair.install.team.watch.insert", tw);
            result.setData(tw);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List list = sqlSession.selectList("gmair.install.team.watch.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData block(String watchId) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.install.team.watch.block", watchId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData remove(String watchId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.install.team.watch.remove", watchId);
        } catch (Exception e) {
            logger.error("[Error] " + e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
