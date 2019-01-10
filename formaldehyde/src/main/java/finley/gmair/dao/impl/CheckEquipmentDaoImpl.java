package finley.gmair.dao.Impl;

import finley.gmair.dao.CheckEquipmentDao;
import finley.gmair.model.formaldehyde.CheckEquipment;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class CheckEquipmentDaoImpl extends BaseDao implements CheckEquipmentDao {

    @Override
    public ResultData insert(CheckEquipment checkEquipment) {
        ResultData result = new ResultData();
        checkEquipment.setEquipmentId(IDGenerator.generate("EQU"));
        try {
            sqlSession.insert("gmair.formaldehyde.check_equipment.insert", checkEquipment);
            result.setData(checkEquipment);
        } catch (Exception e) {
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<CheckEquipment> list = sqlSession.selectList("gmair.formaldehyde.check_equipment.query", condition);
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
