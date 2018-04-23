package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.OrderLocationRetryCountDao;
import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.location.OrderLocationRetryCountVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class OrderLocationRetryDaoImpl extends BaseDao implements OrderLocationRetryCountDao{

    @Override
    public ResultData insert(OrderLocationRetryCount orderLocationRetryCount) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.order.locationretrycount.insert", orderLocationRetryCount);
            result.setData(orderLocationRetryCount);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertBatch(List<OrderLocationRetryCount> list) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.order.locationretrycount.insertBatch", list);
            result.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderLocationRetryCountVo> list =
                    sqlSession.selectList("gmair.order.locationretrycount.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setData(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(OrderLocationRetryCount orderLocationRetryCount) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.order.locationretrycount.update", orderLocationRetryCount);
            result.setData(orderLocationRetryCount);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData delete(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            if (condition.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            } else {
                sqlSession.delete("gmair.order.locationretrycount.delete", condition);
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
