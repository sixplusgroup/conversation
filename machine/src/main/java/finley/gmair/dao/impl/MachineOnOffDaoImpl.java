package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineOnOffDao;
import finley.gmair.model.machine.Machine_on_off;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachineOnOffDaoImpl extends BaseDao implements MachineOnOffDao {
    @Override
    public ResultData insert(Machine_on_off machine_on_off) {
        ResultData result = new ResultData();
        machine_on_off.setConfigId(IDGenerator.generate("MOC"));
        try {
            sqlSession.insert("gmair.machine.on_off.insert", machine_on_off);
            result.setData(machine_on_off);
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
            List<Machine_on_off> list = sqlSession.selectList("gmair.machine.on_off.query", condition);
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
    public ResultData update(Machine_on_off machine_on_off) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.on_off.update", machine_on_off);
            result.setData(machine_on_off);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
