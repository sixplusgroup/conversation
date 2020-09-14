package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineEfficientFilterDao;
import finley.gmair.dao.MachineEfficientInformationDao;
import finley.gmair.model.machine.MachineEfficientFilter;
import finley.gmair.model.machine.MachineEfficientInformation;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: ck
 * @date: 2020/9/14 13:57
 * @description: TODO
 */

@Repository
public class MachineEfficientInformationDaoImpl extends BaseDao implements MachineEfficientInformationDao {

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineEfficientInformation> machineEfficientInformationList = sqlSession.selectList("gmair.machine.machine_efficient_information.query", condition);
            if (machineEfficientInformationList.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(machineEfficientInformationList);
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
            sqlSession.update("gmair.machine.machine_efficient_information.update", condition);
        } catch (Exception e) {
            e.printStackTrace();
            result.setData(condition);
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData add(MachineEfficientInformation machineEfficientInformation) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.machine_efficient_information.insert", machineEfficientInformation);
            result.setData(machineEfficientInformation);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            System.err.println(new StringBuffer(MachineEfficientInformationDaoImpl.class.getName()).append(" - error - ").append(e.getMessage()).toString());
        }
        return result;
    }
}
