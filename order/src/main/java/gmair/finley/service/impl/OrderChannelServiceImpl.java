package gmair.finley.service.impl;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.order.OrderChannelVo;
import gmair.finley.dao.SalesChannelDao;
import gmair.finley.service.OrderChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OrderChannelServiceImpl implements OrderChannelService{

    @Autowired
    private SalesChannelDao salesChannelDao;

    @Override
    public ResultData create(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = salesChannelDao.insert(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData fetchOrderChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = salesChannelDao.query(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return response;
        } else {
            List<OrderChannelVo> list = (List<OrderChannelVo>) response.getData();
            result.setData(list);
            return result;
        }
    }

    @Override
    public ResultData modifyOrderChannel(OrderChannel orderChannel) {
        ResultData result = new ResultData();
        ResultData response = salesChannelDao.update(orderChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @Override
    public ResultData deleteOrderChannel(String channelId) {
        ResultData result = salesChannelDao.delete(channelId);
        return result;
    }
}
