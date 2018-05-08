package finley.gmair.service.impl;

import finley.gmair.dao.HeartbeatPacketDao;
import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.service.HeartbeatPacketService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HeartbeatPacketServiceImpl implements HeartbeatPacketService {

    @Autowired
    private HeartbeatPacketDao heartbeatPacketDao;

    @Override
    public ResultData createHeartbeatPacket(HeartbeatPacket heartbeatPacket) {
        ResultData result = new ResultData();
        ResultData response = heartbeatPacketDao.insertHeartbeatPacket(heartbeatPacket);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to store heartbeat packet with UID: ").append(heartbeatPacket.getUID()).toString());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
