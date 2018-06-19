package finley.gmair.dao.impl;


import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ExpressOrderDao;
import finley.gmair.model.express.ExpressOrder;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public class ExpressOrderDaoImpl extends BaseDao implements ExpressOrderDao {
    @Override
    public ResultData insertExpressOrder(ExpressOrder order) {
        ResultData result = new ResultData();
        order.setExpressId(IDGenerator.generate("EXO"));
        order.setDeliverTime(new Timestamp(System.currentTimeMillis()));
        try {
            sqlSession.insert("gmair.express.order.insert", order);
            result.setData(order);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryExpressOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ExpressOrder> list = sqlSession.selectList("gmair.express.order.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateExpressOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.express.order.updateBatch",condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
