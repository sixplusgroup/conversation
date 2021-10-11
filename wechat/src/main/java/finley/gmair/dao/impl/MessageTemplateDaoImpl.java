package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MessageTemplateDao;
import finley.gmair.model.wechat.MessageTemplate;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;

@Repository
public class MessageTemplateDaoImpl extends BaseDao implements MessageTemplateDao {
    private Logger logger = LoggerFactory.getLogger(MessageTemplateDaoImpl.class);

    @Transactional
    @Override
    public ResultData insert(MessageTemplate message) {
        ResultData result = new ResultData();
        try {
            sqlSession.insert("gmair.wechat.messagetemplate.insert", message);
            result.setData(message);
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
            MessageTemplate message = sqlSession.selectOne("gmair.wechat.messagetemplate.query", condition);
            if (StringUtils.isEmpty(message)) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData update(MessageTemplate message) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.messagetemplate.update", message);
            result.setData(message);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
