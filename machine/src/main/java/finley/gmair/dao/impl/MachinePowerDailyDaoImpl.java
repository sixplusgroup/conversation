package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachinePowerDailyDao;
import finley.gmair.model.machine.MachinePowerDaily;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachinePowerDailyDaoImpl extends BaseDao implements MachinePowerDailyDao {

    @Override
    public ResultData insert(MachinePowerDaily machinePowerDaily){
        ResultData result = new ResultData();
        machinePowerDaily.setStatusId(IDGenerator.generate("STA"));
        try{
            sqlSession.insert("gmair.machine.machinePowerDaily.insert",machinePowerDaily);
            result.setData(machinePowerDaily);
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
            List<MachinePowerDaily> list = sqlSession.selectList("gmair.machine.machinePowerDaily.query",condition);
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
    public ResultData insertDailyBatch(List<MachinePowerDaily> list) {
        ResultData result = new ResultData();
        for (MachinePowerDaily mpd: list) {
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("STA"));
        }
        try {
            sqlSession.insert("gmair.machine.machinePowerDaily.insertDailyBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
