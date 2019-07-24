package finley.gmair.dao;

import finley.gmair.model.payment.Trade;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface TradeDao {

    ResultData existOrder(String orderId);

    ResultData insert(Trade trade);

    ResultData update(Trade trade);

    ResultData query(Map<String, Object> condition);

    ResultData delete(String tradeId);

}
