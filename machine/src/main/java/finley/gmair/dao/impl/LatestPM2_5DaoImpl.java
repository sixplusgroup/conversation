package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.LatestPM2_5Dao;
import finley.gmair.model.machine.LatestPM2_5;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LatestPM2_5DaoImpl extends BaseDao implements LatestPM2_5Dao {

    @Override
    public ResultData insert(LatestPM2_5 latestPM2_5){
        ResultData result = new ResultData();
        latestPM2_5.setLatestId(IDGenerator.generate("LTP"));
        try{
            sqlSession.insert("gmair.machine.pm2_5_latest.insert",latestPM2_5);
            result.setData(latestPM2_5);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<LatestPM2_5> list = sqlSession.selectList("gmair.machine.pm2_5_latest.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateByMachineId(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.machine.pm2_5_latest.updateByMachineId",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
