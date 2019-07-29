package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TradeDao;
import finley.gmair.model.payment.Trade;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TradeDaoImpl extends BaseDao implements TradeDao {

    @Override
    public ResultData existOrder(String orderId) {
        ResultData result = new ResultData();

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        ResultData res = query(map);

        if(res.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(Boolean.valueOf(false));
            return result;
        }

        if(res.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            List<Trade> list = (List<Trade>) res.getData();
            if(list == null || list.size() == 0) {
                result.setData(Boolean.valueOf(false));
            } else if(list.size() >= 1) {
                result.setData(Boolean.valueOf(true));
            }
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("query error");
        return result;
    }

    @Override
    public ResultData insert(Trade trade) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.payment.trade.insert", trade);
            result.setData(trade);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(Trade trade) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.payment.trade.update", trade);
            result.setData(trade);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            List<Trade> list = sqlSession.selectList("gmair.payment.trade.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
            }
            result.setData(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData delete(String tradeId) {
        ResultData result = new ResultData();
        try {
            sqlSession.delete("gmair.payment.trade.delete",tradeId);
        } catch ( Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

}
