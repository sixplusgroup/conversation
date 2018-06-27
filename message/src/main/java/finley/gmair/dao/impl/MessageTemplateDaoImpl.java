package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.MessageTemplateDao;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class MessageTemplateDaoImpl extends BaseDao implements MessageTemplateDao {

    @Override
    @Transactional
    public ResultData insertMessageTemplate(MessageTemplate template) {
        ResultData result = new ResultData();
        template.setTemplateId(IDGenerator.generate("MTE"));
        try {
            sqlSession.insert("gmair.message.template.insert", template);
            result.setData(template);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData queryMessageTemplate(Map<String, Object> condition) {
        ResultData result = new ResultData();
        try {
            List<MessageTemplate> list = sqlSession.selectList("gmair.message.template.query", condition);
            if (list.isEmpty()) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
            }
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            System.out.println(new StringBuffer("Message").append("-error-").append(e.getMessage()).toString());
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @Override
    public ResultData updateMessageTemplate(MessageTemplate template) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.message.template.update", template);
            result.setData(template);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}
