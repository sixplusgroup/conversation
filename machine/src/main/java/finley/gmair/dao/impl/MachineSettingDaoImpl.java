package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineSettingDao;
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
}
