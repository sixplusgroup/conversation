package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.HeatDailyDao;
import finley.gmair.model.dataAnalysis.HeatHourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class HeatDailyDaoImpl extends BaseDao implements HeatDailyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<HeatHourly> list = sqlSession.selectList("gmair.machine.heat_daily.query",condition);
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
    public ResultData insertBatch(List<HeatHourly> list) {
        ResultData result = new ResultData();
        for (HeatHourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("HEA"));
        try {
            sqlSession.insert("gmair.machine.heat_daily.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
