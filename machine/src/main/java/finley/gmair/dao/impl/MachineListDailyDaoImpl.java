package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineListDailyDao;
import finley.gmair.model.machine.MachineListDaily;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.machine.MachineInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class MachineListDailyDaoImpl extends BaseDao implements MachineListDailyDao {

    @Override
    public ResultData insertMachineListDailyBatch(List<MachineListDaily> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.machine.machine_list_daily.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMachineListView(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MachineListDaily> list = sqlSession.selectList("gmair.machine.machine_list_daily.query", condition);
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

}
