package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.EnterpriseOrderDao;
import finley.gmair.model.order.EnterpriseOrder;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class EnterpriseOrderDaoImpl extends BaseDao implements EnterpriseOrderDao {

    @Override
    public ResultData insert(EnterpriseOrder enterpriseOrder){
        ResultData result = new ResultData();
        enterpriseOrder.setOrderId(IDGenerator.generate("ETO"));
        try{
            sqlSession.insert("gmair.order.enterprise.insert",enterpriseOrder);
            result.setData(enterpriseOrder);
        }catch (Exception e){
            e.printStackTrace();
            result.setDescription(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<EnterpriseOrder> list) {
        ResultData result = new ResultData();
        for (EnterpriseOrder enterpriseOrder: list) {
            if (enterpriseOrder.getOrderId() == null)
                enterpriseOrder.setOrderId(IDGenerator.generate("ETO"));
        }
        try {
            sqlSession.insert("gmair.order.enterprise.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            List<EnterpriseOrderDao> list = sqlSession.selectList("gmair.order.enterprise.query",condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Map<String, Object> condition){
        ResultData result = new ResultData();
        try{
            sqlSession.update("gmair.order.enterprise.update",condition);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
