package gmair.finley.service;


import finley.gmair.model.order.*;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResultData;

import java.util.List;
import java.util.Map;

public interface OrderService {
    ResultData upload(List<GuoMaiOrder> order);

    ResultData uploadOrderItem(List<OrderItem> commodityList);

    ResultData fetch(Map<String, Object> condition, DataTableParam param);

    ResultData fetch(Map<String, Object> condition);

    ResultData assign(GuoMaiOrder order);

    ResultData assignBatchCommodity(List<OrderItem> commodityList);

    ResultData blockOrder(Map<String, Object> condition);

    ResultData create(GuoMaiOrder order);

    ResultData create(OrderMission mission);

    ResultData create(OrderItem commodity);

    ResultData fetchMission4Order(Map<String, Object> condition);

    ResultData fetchStatus();

    ResultData fetchOrderStatus(Map<String, Object> condition);


    ResultData removeCommodity(Map<String, Object> condition);
}
