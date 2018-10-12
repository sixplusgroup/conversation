package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OrderDao;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {

    @Override
    public ResultData insertOrder(DriftOrder order) {
        ResultData result = new ResultData();
        order.setOrderId(IDGenerator.generate("GMO"));
        try {
            sqlSession.insert("gmair.drift.order.insert", order);
            result.setData(order);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftOrder> list = sqlSession.selectList("gmair.drift.order.query", condition);
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
    public ResultData updateOrder(DriftOrder order) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.order.update", order);
            result.setData(order);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData deleteOrder(String orderId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.drift.order.delete", orderId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
