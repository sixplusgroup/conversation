package finley.gmair.dao;

import finley.gmair.model.bill.Channel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelDao {

    ResultData queryChannel(Map<String, Object> condition);

    ResultData insertChannel(Channel channel);

    ResultData updateChannel(Channel channel);

    ResultData deleteChannel(String channelId);

}
