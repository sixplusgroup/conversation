package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.VolumeDailyDao;
import finley.gmair.model.dataAnalysis.VolumeHourly;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class VolumeDailyDaoImpl extends BaseDao implements VolumeDailyDao {

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<VolumeHourly> list = sqlSession.selectList("gmair.machine.volume_daily.query",condition);
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
    public ResultData insertBatch(List<VolumeHourly> list) {
        ResultData result = new ResultData();
        for (VolumeHourly mpd: list)
            if (mpd.getStatusId() == null)
                mpd.setStatusId(IDGenerator.generate("VOL"));
        try {
            sqlSession.insert("gmair.machine.volume_daily.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
