package gmair.finley.dao.impl;

import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.dao.BaseDao;
import gmair.finley.dao.OrderDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class OrderDaoImpl extends BaseDao implements OrderDao {
    @Override
    public ResultData insertOrder(PlatformOrder order) {
        ResultData result = new ResultData();
        order.setOrderId(IDGenerator.generate("ODR"));
        try {
            sqlSession.insert("gmair.order.insert", order);
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
