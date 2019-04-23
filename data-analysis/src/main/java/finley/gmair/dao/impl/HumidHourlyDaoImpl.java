package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.HumidHourlyDao;
import finley.gmair.model.dataAnalysis.HumidHourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class HumidHourlyDaoImpl extends BaseDao implements HumidHourlyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<HumidHourly> list = sqlSession.selectList("gmair.machine.humid_hourly.query",condition);
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
    public ResultData insertBatch(List<HumidHourly> list) {
        ResultData result = new ResultData();
        for (HumidHourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("HUD"));
        try {
            sqlSession.insert("gmair.machine.humid_hourly.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}