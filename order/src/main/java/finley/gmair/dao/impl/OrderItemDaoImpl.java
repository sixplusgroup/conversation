package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OrderItemDao;
import finley.gmair.model.order.OrderItem;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {

    @Override
    public ResultData insert(OrderItem item, String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> value = new HashMap<>();
        item.setItemId(IDGenerator.generate("ODT"));
        value.put("item", item);
        value.put("orderId", orderId);
        try {
            sqlSession.insert("gmair.order.platform.item.insert", value);
            result.setData(item);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderItem> list = sqlSession.selectList("gmair.order.platform.item.query", condition);
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
    public ResultData update(OrderItem item) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.order.platform.item.update", item);
            result.setData(item);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData delete(String itemId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.order.platform.item.delete", itemId);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
