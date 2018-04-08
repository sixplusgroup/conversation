package finley.gmair.dao.impl;

import finley.gmair.dao.MonitorStationDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MonitorStationDaoImpl extends BaseDao implements MonitorStationDao {

    @Override
    public ResultData insert(MonitorStation monitorStation) {
        ResultData result = new ResultData();
        monitorStation.setStationId(IDGenerator.generate("ms"));
        try {
            sqlSession.insert("gmair.airquality.monitorstation.insert", monitorStation);
            result.setData(monitorStation);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<MonitorStation> list) {
        ResultData result = new ResultData();
        for (MonitorStation monitorStation: list) {
            monitorStation.setStationId(IDGenerator.generate("ms"));
        }
        try {
            sqlSession.insert("gmair.airquality.monitorstation.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
