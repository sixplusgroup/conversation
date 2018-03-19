package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MessageDao;
import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MessageDaoImpl extends BaseDao implements MessageDao {
    @Override
    public ResultData insert(TextMessage message) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }
}
