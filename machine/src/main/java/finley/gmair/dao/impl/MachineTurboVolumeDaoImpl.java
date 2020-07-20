package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineTurboVolumeDao;
import finley.gmair.model.machine.MachineTurboVolume;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/18 15:35
 * @description: TODO
 */

@Repository
public class MachineTurboVolumeDaoImpl extends BaseDao implements MachineTurboVolumeDao {
    @Override
    public ResultData add(MachineTurboVolume machineTurboVolume) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.machine_turbo_volume.insert", machineTurboVolume);
            result.setData(machineTurboVolume);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.err.println(new StringBuffer(MachineTurboVolumeDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.machine_turbo_volume.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineTurboVolume> machineTurboVolumeList = sqlSession.selectList("gmair.machine.machine_turbo_volume.query", condition);
            if (machineTurboVolumeList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            else if (machineTurboVolumeList.size()==1){
                result.setData(machineTurboVolumeList.get(0));
            }
            else {
                result.setData(machineTurboVolumeList);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
