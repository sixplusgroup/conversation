package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineStatusStatisticsDao;
import finley.gmair.model.machine.MachinePm2_5;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachinePm2_5Vo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachineStatusStatisticsDaoImpl extends BaseDao implements MachineStatusStatisticsDao{

    @Override
    public ResultData insertHourlyBatch(List<MachinePm2_5> list) {
        ResultData result = new ResultData();
        for (MachinePm2_5 machinePm2_5: list) {
            if (machinePm2_5.getStatusId() == null)
                machinePm2_5.setStatusId(IDGenerator.generate("mas"));
        }
        try {
            sqlSession.insert("gmair.airquality.machinepm25.insertHourlyBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData selectHourly(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachinePm2_5Vo> list = sqlSession.selectList("gmair.airquality.machinepm25.selectHourly", condition);
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
    public ResultData insertDailyBatch(List<MachinePm2_5> list) {
        ResultData result = new ResultData();
        for (MachinePm2_5 machinePm2_5: list) {
            if (machinePm2_5.getStatusId() == null)
                machinePm2_5.setStatusId(IDGenerator.generate("mas"));
        }
        try {
            sqlSession.insert("gmair.airquality.machinepm25.insertDailyBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData selectDaily(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachinePm2_5Vo> list = sqlSession.selectList("gmair.airquality.machinepm25.selectDaily", condition);
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
    public ResultData insertMonthlyBatch(List<MachinePm2_5> list) {
        ResultData result = new ResultData();
        for (MachinePm2_5 machinePm2_5: list) {
            if (machinePm2_5.getStatusId() == null)
                machinePm2_5.setStatusId(IDGenerator.generate("mas"));
        }
        try {
            sqlSession.insert("gmair.airquality.machinepm25.insertMonthlyBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData selectMonthly(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachinePm2_5Vo> list = sqlSession.selectList("gmair.airquality.machinepm25.selectMonthly", condition);
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
