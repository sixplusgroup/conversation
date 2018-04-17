package finley.gmair.service.impl;

import finley.gmair.dao.ChannelDao;
import finley.gmair.model.order.OrderChannel;
import finley.gmair.service.ChannelService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ChannelServiceImpl implements ChannelService {
    @Autowired
    private ChannelDao channelDao;

    @Override
    public ResultData fetchChannel(Map<String, Object> condition) {
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
    public ResultData createChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("channelName", channel.getChannelName());
        condition.put("blockFlag", false);
        ResultData response = channelDao.queryChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Channel with name: ").append(channel.getChannelName()).append(" already exist").toString());
            return result;
        }
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to finish the prerequisite check");
            return result;
        }
        response = channelDao.insertChannel(channel);
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
    public ResultData reviseChannel(OrderChannel channel) {
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
