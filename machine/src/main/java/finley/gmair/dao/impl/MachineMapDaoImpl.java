package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MachineMapDao;
import finley.gmair.model.machine.MachineLoc;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MachineMapDaoImpl extends BaseDao implements MachineMapDao {

    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try {
            List<MachineLoc> list = sqlSession.selectList("gmair.machine.consumer_code_machine_view.query", condition);
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
