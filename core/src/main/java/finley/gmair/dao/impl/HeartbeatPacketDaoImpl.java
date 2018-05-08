package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.HeartbeatPacketDao;
import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

@Repository
public class HeartbeatPacketDaoImpl extends BaseDao implements HeartbeatPacketDao {

    @Override
    public ResultData insertHeartbeatPacket(HeartbeatPacket heartbeatPacket) {
        ResultData result = new ResultData();
        try {
              mongoTemplate.insert(heartbeatPacket, "heartbeat_packet");
              result.setData(heartbeatPacket);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
