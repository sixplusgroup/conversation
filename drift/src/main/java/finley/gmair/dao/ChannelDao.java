package finley.gmair.dao;

import finley.gmair.model.drift.DriftChannel;
import finley.gmair.util.ResultData;

import java.util.Map;

public interface ChannelDao {
    ResultData insert(DriftChannel channel);

    ResultData query(Map<String, Object> condition);
}
