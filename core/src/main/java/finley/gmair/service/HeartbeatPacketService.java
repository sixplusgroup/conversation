package finley.gmair.service;

import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.util.ResultData;

public interface HeartbeatPacketService {

    ResultData createHeartbeatPacket(HeartbeatPacket heartbeatPacket);

}
