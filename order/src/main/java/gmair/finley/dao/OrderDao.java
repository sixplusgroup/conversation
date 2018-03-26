package gmair.finley.dao;

import finley.gmair.model.order.GuoMaiOrder;
import finley.gmair.model.order.OrderItem;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    ResultData insert(List<GuoMaiOrder> order);

    ResultData insertOrderItem(List<OrderItem> commodityList);

    ResultData query(Map<String, Object> condition);

    ResultData query(Map<String, Object> condition, DataTableParam param);

    ResultData update(GuoMaiOrder order);

    ResultData blockOrder(Map<String, Object> condition);

    ResultData create(GuoMaiOrder order);

    ResultData create(OrderItem commodity);

    ResultData channel();

    ResultData status();

    ResultData queryOrderStatus(Map<String, Object> condition);

    ResultData updateBatchCommodity(List<OrderItem> commodity);

    ResultData blockCommodity(Map<String, Object> condition);
}

