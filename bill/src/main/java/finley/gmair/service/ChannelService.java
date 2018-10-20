package finley.gmair.service;

import finley.gmair.model.bill.Channel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelService {

    ResultData fetchChannel(Map<String, Object> condition);

    ResultData createChannel(Channel channel);

    ResultData updateChannel(Channel channel);

    ResultData deleteChannel(String channelID);
}
