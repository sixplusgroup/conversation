package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ChannelDao;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import finley.gmair.model.bill.Channel;
import java.util.List;
import java.util.Map;

@Repository
public class ChannelDaoImpl extends BaseDao implements ChannelDao {
    @Override
    public ResultData queryChannel(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try{
            List<finley.gmair.model.bill.Channel> list = sqlSession.selectList("gmair.channel.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData insertChannel(Channel channel) {
        ResultData result = new ResultData();
        channel.setChannelId(IDGenerator.generate("CHAN"));
        try {
            sqlSession.insert("gmair.bill.channel.insert",channel);
            result.setData(channel);
        }catch (Exception e){
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateChannel(Channel channel) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.bill.channel.update", channel);
            result.setData(channel);
        } catch (Exception e) {
            e.printStackTrace();
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
        public  ResultData deleteChannel(String channelId) {
            ResultData result = new ResultData();
            try {
                sqlSession.delete("gmair.bill.channel.delete",channelId);
            } catch ( Exception e) {
                e.printStackTrace();
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
            return result;
        }

    }