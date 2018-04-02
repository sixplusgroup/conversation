package gmair.finley.dao.impl;

import finley.gmair.model.order.OrderChannel;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.dao.BaseDao;
import gmair.finley.dao.ChannelDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ChannelDaoImpl extends BaseDao implements ChannelDao {
    @Override
    public ResultData queryChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<OrderChannel> list = sqlSession.selectList("gmair.order.platform.channel.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        channel.setChannelId(IDGenerator.generate("OCL"));
        try {
            sqlSession.insert("gmair.order.platform.channel.insert", channel);
            result.setData(channel);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateChannel(OrderChannel channel) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.order.platform.channel.update", channel);
            result.setData(channel);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
