package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MessageDao;
import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MessageDaoImpl extends BaseDao implements MessageDao {

    private Object lock = new Object();

    @Override
    public ResultData insert(TextMessage message) {
        ResultData result = new ResultData();
        synchronized (lock) {
            try {
                sqlSession.insert("gmair.message.insert", message);
                result.setData(message);
            } catch (Exception e) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription(e.getMessage());
            }
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<TextMessage> list = sqlSession.selectList("gmair.message.query", condition);
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
}
