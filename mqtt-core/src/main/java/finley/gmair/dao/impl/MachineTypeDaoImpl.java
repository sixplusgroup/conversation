package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineTypeDao;
import finley.gmair.model.mqtt.MachineType;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachineTypeDaoImpl extends BaseDao implements MachineTypeDao {

    @Override
    public ResultData queryType(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineType> list = sqlSession.selectList("gmair.mqtt.machineType.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertType(MachineType machineType) {
        ResultData result = new ResultData();
        machineType.setBoardId(IDGenerator.generate("MTI"));
        try {
            sqlSession.insert("gmair.mqtt.machineType.insert", machineType);
            result.setData(machineType);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
