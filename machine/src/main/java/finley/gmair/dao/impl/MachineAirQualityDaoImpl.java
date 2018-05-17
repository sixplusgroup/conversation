package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineAirQualityDao;
import finley.gmair.model.air.MachineAirQuality;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MachineAirQualityDaoImpl extends BaseDao implements MachineAirQualityDao {

    @Override
    public ResultData insert(MachineAirQuality machineAirQuality) {
        if (machineAirQuality.getMaId() == null) {
            machineAirQuality.setMaId(IDGenerator.generate("MAQ"));
        }
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.machineairquality.insert", machineAirQuality);
            result.setData(machineAirQuality);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<MachineAirQuality> list) {
        for (MachineAirQuality machineAirQuality : list) {
            if (machineAirQuality.getMaId() == null) {
                machineAirQuality.setMaId(IDGenerator.generate("MAQ"));
            }
        }
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.airquality.machineairquality.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
