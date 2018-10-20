package finley.gmair.dao;

import finley.gmair.util.ResultData;

import java.nio.channels.Channel;
import java.util.Map;

public interface ChannelDao {

    ResultData queryChannel(Map<String, Object> condition);

    ResultData insertChannal(Channel channel);

    ResultData updateChannal(Channel channel);
}
