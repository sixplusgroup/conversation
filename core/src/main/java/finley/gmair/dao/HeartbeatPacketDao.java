package finley.gmair.dao;

import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.util.ResultData;

public interface HeartbeatPacketDao {

    ResultData insertHeartbeatPacket(HeartbeatPacket heartbeatPacket);

}
