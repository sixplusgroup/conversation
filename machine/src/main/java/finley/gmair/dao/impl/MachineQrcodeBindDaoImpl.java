package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineQrcodeBindDao;
import finley.gmair.model.machine.MachineQrcodeBind;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineQrcodeBindVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MachineQrcodeBindDaoImpl extends BaseDao implements MachineQrcodeBindDao {

    @Override
    public ResultData insert(MachineQrcodeBind machineQrcodeBind) {
        if (machineQrcodeBind.getBindId() == null) {
            machineQrcodeBind.setBindId(IDGenerator.generate("mqb"));
        }
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machineqrcodebind.insert", machineQrcodeBind);
            result.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData select(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineQrcodeBindVo> list = sqlSession.selectList("gmair.machineqrcodebind.select", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
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
            sqlSession.update("gmair.machineqrcodebind.update",condition);

        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData delete(String bindId){
        ResultData result = new ResultData();
        try{
            sqlSession.delete("gmair.machineqrcodebind.delete",bindId);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
