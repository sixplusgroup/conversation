package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.HeartbeatPacketDao;
import finley.gmair.model.core.HeartbeatPacket;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public ResultData queryHeartbeatPacket(String UID) {
        ResultData result = new ResultData();
        try {

            //mongoTemplate = new MongoTemplate(mongoDbFactory, (MongoConverter) timestampConverter);
            List<HeartbeatPacket> list = mongoTemplate.find(new Query(Criteria.where("UID").is(UID)), HeartbeatPacket.class, "heartbeat_packet");
             if(list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            System.out.println(e.getMessage());
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
