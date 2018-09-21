package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ReceiveMessageDao;
import finley.gmair.model.message.TextMessage;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ReceiveMessageDaoImpl extends BaseDao implements ReceiveMessageDao {

    @Override
    public ResultData insert(TextMessage message) {
        ResultData result = new ResultData();
        try {

        } catch (Exception e) {

        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {

        } catch (Exception e) {

        }
        return result;
    }
}
