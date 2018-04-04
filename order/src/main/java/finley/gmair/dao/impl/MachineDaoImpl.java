package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineDao;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineInstallTypeVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MachineDaoImpl extends BaseDao implements MachineDao{

    @Override
    public ResultData queryInstallType(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineInstallTypeVo> list = sqlSession.selectList("gmair.machine.machineInstall.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
