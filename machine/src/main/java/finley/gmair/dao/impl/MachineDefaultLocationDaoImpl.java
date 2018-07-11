package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineDefaultLocationDao;
import finley.gmair.model.machine.MachineDefaultLocation;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MachineDefaultLocationDaoImpl extends BaseDao implements MachineDefaultLocationDao {
    @Override
    public ResultData insert(MachineDefaultLocation machineDefaultLocation) {
        ResultData result = new ResultData();
        machineDefaultLocation.setLocationId(IDGenerator.generate("MDL"));
        try {
            sqlSession.insert("gmair.machine.machine_default_location.insert", machineDefaultLocation);
            result.setData(machineDefaultLocation);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineDefaultLocation> list = sqlSession.selectList("gmair.machine.machine_default_location.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.machine_default_location.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}