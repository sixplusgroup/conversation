package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DriftOrderCancelDao;
import finley.gmair.model.drift.DriftAddress;
import finley.gmair.model.drift.DriftOrderCancel;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DriftOrderCancelDaoImpl extends BaseDao implements DriftOrderCancelDao {
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftAddress> list = sqlSession.selectList("gmair.drift.drift_order_cancel.query", condition);
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

    @Override
    public ResultData insert(DriftOrderCancel driftOrderCancel) {
        ResultData result = new ResultData();
        driftOrderCancel.setCancelId(IDGenerator.generate("CAN"));
        try {
            sqlSession.insert("gmair.drift.drift_order_cancel.insert", driftOrderCancel);
            result.setData(driftOrderCancel);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(DriftOrderCancel driftOrderCancel) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.drift_order_cancel.update", driftOrderCancel);
            result.setData(driftOrderCancel);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
