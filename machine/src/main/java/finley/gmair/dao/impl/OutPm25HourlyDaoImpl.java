package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OutPm25HourlyDao;
import finley.gmair.model.machine.OutPm25Hourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OutPm25HourlyDaoImpl extends BaseDao implements OutPm25HourlyDao {

    @Override
    public ResultData insert(OutPm25Hourly outPm25Hourly){
        ResultData result = new ResultData();
        outPm25Hourly.setLatestId(IDGenerator.generate("LTP"));
        try{
            sqlSession.insert("gmair.machine.outPm25Hourly.insert", outPm25Hourly);
            result.setData(outPm25Hourly);
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
            List<OutPm25Hourly> list = sqlSession.selectList("gmair.machine.outPm25Hourly.query",condition);
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
            sqlSession.update("gmair.machine.outPm25Hourly.updateByMachineId",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
