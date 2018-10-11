package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OrderItemDao;
import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {

    @Override
    public ResultData queryOrderItem(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<DriftOrderItem> list = sqlSession.selectList("gmair.drift.order.item.query", condition);
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
    public ResultData insertOrderItem(DriftOrderItem item) {
        ResultData result = new ResultData();
        item.setItemId(IDGenerator.generate("GMD"));
        try {
            sqlSession.insert("gmair.drift.order.item.insert", item);
            result.setData(item);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateOrderItem(DriftOrderItem item) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.drift.order.item.update", item);
            result.setData(item);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData deleteOrderItem(String itemId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.drift.order.item.delete", itemId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
