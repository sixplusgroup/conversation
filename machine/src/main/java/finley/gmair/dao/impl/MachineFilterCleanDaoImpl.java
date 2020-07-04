package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author: Bright Chan
 * @date: 2020/7/4 13:57
 * @description: TODO
 */

@Repository
public class MachineFilterCleanDaoImpl extends BaseDao implements MachineFilterCleanDao {
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        String qrcode=(String)condition.get("qrcode");
        try {
            MachineFilterClean machineFilterClean = sqlSession.selectOne("gmair.machine.machine_filter_clean.query", qrcode);
            if (StringUtils.isEmpty(machineFilterClean)) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineFilterClean);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.machine_filter_clean.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
