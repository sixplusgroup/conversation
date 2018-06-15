package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineSettingDao;
import finley.gmair.model.machine.v2.MachineSetting;
import finley.gmair.model.machine.v2.VolumeSetting;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.PowerSettingVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MachineSettingDaoImpl extends BaseDao implements MachineSettingDao{

    @Override
    public ResultData selectPowerSetting(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<PowerSettingVo> list = sqlSession.selectList("gmair.machinesetting.selectpowersetting", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData queryMachineSetting(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineSetting> list = sqlSession.selectList("gmair.machinesetting.queryMachineSetting", condition);
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
    public ResultData selectVolumeSetting(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<VolumeSetting> list = sqlSession.selectList("gmair.machine.volumesetting.query", condition);
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
    public ResultData insertVolumeSetting(VolumeSetting setting) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.volumesetting.insert", setting);
            result.setData(setting);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateVolumeSetting(VolumeSetting setting) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.volumesetting.update", setting);
            result.setData(setting);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
