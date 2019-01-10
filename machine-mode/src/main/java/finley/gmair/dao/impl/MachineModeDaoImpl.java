package finley.gmair.dao.Impl;

import finley.gmair.dao.MachineModeDao;
import finley.gmair.model.mode.MachineMode;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachineModeDaoImpl extends BaseDao implements MachineModeDao {

    @Override
    public ResultData insert(MachineMode machineMode){
        ResultData result = new ResultData();
        machineMode.setModeId(IDGenerator.generate("MDD"));
        try{
            sqlSession.insert("gmair.machine_mode.insert",machineMode);
            result.setData(machineMode);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<MachineMode> list = sqlSession.selectList("gmair.machine_mode.query",condition);
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
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.machine_mode.update",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
