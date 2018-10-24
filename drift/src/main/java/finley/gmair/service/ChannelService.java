package finley.gmair.service;

import finley.gmair.model.drift.DriftChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelService {
    ResultData createChannel(DriftChannel channel);

    ResultData fetchChannel(Map<String, Object> condition);
}
