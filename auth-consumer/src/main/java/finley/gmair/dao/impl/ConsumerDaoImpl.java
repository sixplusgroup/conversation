package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.ConsumerDao;
import finley.gmair.form.consumer.ConsumerPartInfoQuery;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.consumer.ConsumerPartInfoVo;
import finley.gmair.vo.consumer.ConsumerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ConsumerDaoImpl extends BaseDao implements ConsumerDao {

    private Logger logger = LoggerFactory.getLogger(ConsumerDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(Consumer consumer) {
        ResultData result = new ResultData();
        consumer.setConsumerId(IDGenerator.generate("CSR"));
        try {
            sqlSession.insert("gmair.consumer.insert", consumer);
            result.setData(consumer);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<ConsumerVo> list = sqlSession.selectList("gmair.consumer.query", condition);
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
    public List<ConsumerPartInfoVo> queryConsumerAccounts(ConsumerPartInfoQuery query) {
        try {
            return sqlSession.selectList("gmair.consumer.queryConsumerAccounts", query);
        } catch (Exception e) {
            logger.error("query consumer accounts failed: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public ResultData update(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.consumer.update", condition);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
