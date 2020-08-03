package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineEfficientFilterDao;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: ck
 * @date: 2020/7/25 13:57
 * @description: TODO
 */

@Repository
public class MachineEfficientFilterDaoImpl extends BaseDao implements MachineEfficientFilterDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineEfficientFilter> machineEfficientFilterList = sqlSession.selectList("gmair.machine.machine_efficient_filter.query", condition);
            if (machineEfficientFilterList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineEfficientFilterList);
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
            sqlSession.update("gmair.machine.machine_efficient_filter.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryNeedRemindFirst() {
        ResultData result = new ResultData();
        try {
            List<MachineEfficientFilter> machineEfficientFilterList = sqlSession.selectList("gmair.machine.machine_efficient_filter.query_need_remind_first");
            if (machineEfficientFilterList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineEfficientFilterList);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryNeedRemindSecond() {
        ResultData result = new ResultData();
        try {
            List<MachineEfficientFilter> machineEfficientFilterList = sqlSession.selectList("gmair.machine.machine_efficient_filter.query_need_remind_second");
            if (machineEfficientFilterList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineEfficientFilterList);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData add(MachineEfficientFilter machineEfficientFilter) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.machine_efficient_filter.insert", machineEfficientFilter);
            result.setData(machineEfficientFilter);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.err.println(new StringBuffer(MachineEfficientFilterDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }
}
