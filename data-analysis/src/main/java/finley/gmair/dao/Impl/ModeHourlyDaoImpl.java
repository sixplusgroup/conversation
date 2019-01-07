package finley.gmair.dao.Impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ModeHourlyDao;
import finley.gmair.model.dataAnalysis.ModeHourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ModeHourlyDaoImpl extends BaseDao implements ModeHourlyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<ModeHourly> list = sqlSession.selectList("gmair.machine.mode_hourly.query",condition);
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
    public ResultData insertBatch(List<ModeHourly> list) {
        ResultData result = new ResultData();
        for (ModeHourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("MOD"));
        try {
            sqlSession.insert("gmair.machine.mode_hourly.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
