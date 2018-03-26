package gmair.finley.service.impl;

import finley.gmair.model.order.OrderDiversion;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.service.OrderDiversionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by hushe on 2018/1/22.
 */
@Service
public class OrderDiversionServiceImpl implements OrderDiversionService {
    private Logger logger = LoggerFactory.getLogger(OrderDiversionServiceImpl.class);

    @Autowired
    private OrderDiversionDao orderDiversionDao;

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<OrderDiversionVo> list = (List<OrderDiversionVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData create(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionDao.insert(orderDiversion);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData modify(OrderDiversion orderDiversion) {
        ResultData result = new ResultData();
        ResultData response = orderDiversionDao.update(orderDiversion);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData delete(String diversionId) {
        ResultData result = orderDiversionDao.delete(diversionId);
        return result;
    }
}
