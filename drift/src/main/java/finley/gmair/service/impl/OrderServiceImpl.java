package finley.gmair.service.impl;

import finley.gmair.dao.OrderDao;
import finley.gmair.dao.OrderItemDao;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftOrderItem;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public ResultData fetchDriftOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = orderDao.queryOrder(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No drift order found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query drift order");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    /**
     * This method will save the drift order to database
     * step 1: save the drift order
     * step 2: save the order item list with orderId
     *
     * @param order
     * @return
     */
    @Override
    public ResultData createDriftOrder(DriftOrder order) {
        ResultData result = new ResultData();
        //first insert order, then insert order item
        ResultData response = orderDao.insertOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert drift order with: " + order.toString());
            return result;
        }
        String orderId = ((DriftOrder) response.getData()).getOrderId();
        //insert order item
        DriftOrderItem item = order.getItem();
        if (!StringUtils.isEmpty(item.getItemName())) {
            item.setOrderId(orderId);
            new Thread(() -> {
                orderItemDao.insertOrderItem(item);
            }).start();
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData updateDriftOrder(DriftOrder order) {
        ResultData result = new ResultData();
        ResultData response = orderDao.updateOrder(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update drift order with orderId: ")
                    .append(order.getOrderId()).append(" to ").append(order.toString()).toString());
        }
        return result;
    }

    @Override
    public ResultData deleteDriftOrder(String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("orderId", orderId);
        ResultData response = orderItemDao.queryOrderItem(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query drift order item");
            return result;
        }
        List<DriftOrderItem> list = (List<DriftOrderItem>) response.getData();
        new Thread(() -> {
            for (DriftOrderItem item : list) {
                orderItemDao.deleteOrderItem(item.getItemId());
            }
        }).start();
        result = orderDao.deleteOrder(orderId);
        return result;
    }
}
