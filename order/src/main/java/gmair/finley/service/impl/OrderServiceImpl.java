package gmair.finley.service.impl;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.dao.ChannelDao;
import gmair.finley.dao.OrderDao;
import gmair.finley.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ChannelDao channelDao;

    @Override
    public ResultData fetchPlatformOrderChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = channelDao.queryChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No channel information found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find channel information");
        }
        return result;
    }

    @Override
    public ResultData createPlatformOrderChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        ResultData response = channelDao.insertChannel(channel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert channel: " + channel.toString());
        }
        return result;
    }

    @Override
    public ResultData updatePlatformOrderChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        ResultData response = channelDao.updateChannel(channel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update channel with channel_id: ").append(channel.getChannelId()).append(" to: ").append(channel.toString()).toString());
        }
        return result;
    }
}
