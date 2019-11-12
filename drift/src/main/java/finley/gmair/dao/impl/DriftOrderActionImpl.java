package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.DriftOrderActionDao;
import finley.gmair.model.drift.DriftOrderAction;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class DriftOrderActionImpl extends BaseDao implements DriftOrderActionDao {
    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftOrderAction> list = sqlSession.selectList("gmair.drift.orderaction.query", condition);
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
    public ResultData insert(DriftOrderAction driftOrderAction) {
        ResultData result = new ResultData();
        driftOrderAction.setActionId(IDGenerator.generate("DOA"));
        try {
            sqlSession.insert("gmair.drift.orderaction.insert", driftOrderAction);
            result.setData(driftOrderAction);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
