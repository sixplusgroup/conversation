package finley.gmair.service.impl;

import finley.gmair.dao.ChannelDao;
import finley.gmair.model.bill.Channel;
import finley.gmair.service.ChannelService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelDao channelDao;

    @Override
    public ResultData fetchChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = channelDao.queryChannel(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No channel found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query channel");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @Override
    public ResultData createChannel(Channel channel) {

        ResultData result = new ResultData();
        ResultData response = channelDao.insertChannel(channel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert channel with: " + channel.toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @Override
    public ResultData updateChannel(Channel channel) {

        ResultData result = new ResultData();
        ResultData response = channelDao.updateChannel(channel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setDescription(new StringBuffer("Fail to update channel with channelId: ")
                    .append(channel.getChannelId()).append(" to ").append(channel.toString()).toString());
        }
        return result;
    }

    @Override
    public ResultData deleteChannel(String channelId) {

        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("channelId", channelId);
        ResultData response = channelDao.queryChannel(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query channel");
            return result;
        }
        result = channelDao.deleteChannel(channelId);
        return result;
    }
}
