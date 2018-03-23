package finley.gmair.dao.impl;

import finley.gmair.dao.AutoReplyDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.wechat.AutoReply;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class AutoReplyDaoImpl extends BaseDao implements AutoReplyDao {
    private Logger logger = LoggerFactory.getLogger(AutoReplyDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(AutoReply autoReply) {
        ResultData result = new ResultData();
        autoReply.setReplyId(IDGenerator.generate("ARI"));
        try {
            sqlSession.insert("gmair.wechat.autoreply.insert", autoReply);
            result.setData(autoReply);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData query(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<AutoReply> list = sqlSession.selectList("gmair.wechat.autoreply.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(AutoReply autoReply) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.autoreply.update", autoReply);
            result.setData(autoReply);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return null;
    }
}