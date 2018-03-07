package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ConsumerDao;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConsumerDaoImpl extends BaseDao implements ConsumerDao {
    @Override
    public ResultData insert(Consumer consumer) {
        ResultData result = new ResultData();
        consumer.setConsumerId(IDGenerator.generate("CSR"));
        try {
            sqlSession.insert("gmair.consumer.insert", consumer);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();

        return result;
    }

    @Override
    public ResultData update(Consumer consumer) {
        ResultData result = new ResultData();

        return result;
    }
}
