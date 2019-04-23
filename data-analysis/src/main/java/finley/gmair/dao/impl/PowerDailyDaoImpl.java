package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.PowerDailyDao;
import finley.gmair.model.dataAnalysis.PowerHourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class PowerDailyDaoImpl extends BaseDao implements PowerDailyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<PowerHourly> list = sqlSession.selectList("gmair.machine.power_daily.query",condition);
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
    public ResultData insertBatch(List<PowerHourly> list) {
        ResultData result = new ResultData();
        for (PowerHourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("POW"));
        try {
            sqlSession.insert("gmair.machine.power_daily.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
