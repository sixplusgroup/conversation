package gmair.finley.service.impl;


import finley.gmair.model.order.*;
import finley.gmair.pagination.DataTableParam;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.CommodityVo;
import finley.gmair.vo.order.OrderChannelVo;
import finley.gmair.vo.order.OrderStatusVo;
import gmair.finley.dao.*;
import gmair.finley.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class OrderServiceImpl implements OrderService {
	private Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderMissionDao orderMissionDao;



	@Autowired
    private CommodityDao commodityDao;

	@Autowired
    private MachineItemDao machineItemDao;

	@Override
	public ResultData upload(List<GuoMaiOrder> order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.insert(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ResultData uploadOrderItem(List<OrderItem> commodityList) {
		ResultData result = new ResultData();
		ResultData response = orderDao.insertOrderItem(commodityList);
		//create machine item with the order if order item has machine
        List<OrderItem> orderItems = (List<OrderItem>) response.getData();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            Map<String, Object> condition = new HashMap<>();
            condition.put("blockFlag", 0);
            List<CommodityVo> commodityVoList = (List<CommodityVo>) commodityDao.query(condition).getData();
            Map<String, CommodityType> commodityTypeMap =
                    commodityVoList.stream().collect(Collectors.toMap(e -> e.getCommodityId(), e -> e.getType()));
            List<MachineItem> machineItemList = new ArrayList<>();
            for (OrderItem item : orderItems) {
                for (int i = 0; i < item.getCommodityQuantity(); i++) {
                    if (commodityTypeMap.get(item.getCommodityId()) == CommodityType.GUOMAI_XINFENG) {
                        MachineItem machineItem = new MachineItem();
                        machineItem.setOrderItemId(item.getOrderItemId());
                        machineItemList.add(machineItem);
                    }
                }
            }
            if (!machineItemList.isEmpty())
                machineItemDao.insertBatch(machineItemList);
        }
		return result;
	}

	@Override
	public ResultData fetch(Map<String, Object> condition, DataTableParam param) {
		ResultData result = new ResultData();
		ResultData response = orderDao.query(condition, param);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetch(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData assign(GuoMaiOrder order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.update(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData blockOrder(Map<String, Object> condition) {
		return orderDao.blockOrder(condition);
	}

	@Override
	public ResultData create(GuoMaiOrder order) {
		ResultData result = new ResultData();
		ResultData response = orderDao.create(order);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	public ResultData create(OrderMission mission) {
		ResultData result = new ResultData();
		ResultData response = orderMissionDao.insert(mission);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData create(OrderItem commodity) {
		return orderDao.create(commodity);
	}

	@Override
	public ResultData fetchMission4Order(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderMissionDao.query(condition);
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

	@Override
	public ResultData fetchStatus() {
		ResultData result = new ResultData();
		ResultData response = orderDao.status();
		result.setResponseCode(response.getResponseCode());
		if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
			result.setData(response.getData());
		} else {
			result.setDescription(response.getDescription());
		}
		return result;
	}

    @Override
    public ResultData fetchOrderStatus(Map<String, Object> condition) {
		ResultData result = new ResultData();
		ResultData response = orderDao.queryOrderStatus(condition);
		if (response.getResponseCode() != ResponseCode.RESPONSE_OK){
			return response;
		} else {
			List<OrderStatusVo> list = (List<OrderStatusVo>) response.getData();
			result.setData(list);
			return result;
		}
    }

	@Override
	public ResultData assignBatchCommodity(List<OrderItem> commodityList) {
		return orderDao.updateBatchCommodity(commodityList);
	}

	@Override
	public ResultData removeCommodity(Map<String, Object> condition) {
		return orderDao.blockCommodity(condition);
	}
}
