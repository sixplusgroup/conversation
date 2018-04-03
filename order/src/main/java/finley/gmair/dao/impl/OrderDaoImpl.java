package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OrderDao;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {
    @Override
    public ResultData insertOrder(PlatformOrder order) {
        ResultData result = new ResultData();
        order.setOrderId(IDGenerator.generate("ODR"));
        try {
            sqlSession.insert("gmair.order.platform.insert", order);
            result.setData(order);
        }catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryOrder(Map<String, Object> condition) {
        return null;
    }

    @Override
    public ResultData updateOrder(Map<String, Object> condition) {
        return null;
    }
}
