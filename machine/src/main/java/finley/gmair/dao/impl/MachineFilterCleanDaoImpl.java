package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineFilterCleanDao;
import finley.gmair.model.machine.MachineFilterClean;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
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
        return null;
    }

    @Override
    public ResultData queryOne(Map<String, Object> condition) {
        ResultData result = new ResultData();
        String qrcode=(String)condition.get("qrcode");
        try {
            MachineFilterClean machineFilterClean = sqlSession.selectOne("gmair.machine.machine_filter_clean.query", qrcode);
            if (machineFilterClean == null || StringUtils.isEmpty(machineFilterClean)) {
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

    @Override
    public ResultData add(MachineFilterClean machineFilterClean) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.machine_filter_clean.insert", machineFilterClean);
            result.setData(machineFilterClean);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.err.println(new StringBuffer(MachineFilterCleanDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }

    @Override
    public ResultData queryNeedRemind() {
        ResultData result = new ResultData();
        try {
            List<MachineFilterClean> machineFilterCleanList = sqlSession.selectList("gmair.machine.machine_filter_clean.query_need_remind");
            if (StringUtils.isEmpty(machineFilterCleanList)) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineFilterCleanList);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateIsNeedClean(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.machine.machine_filter_clean.update_is_need_clean", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
