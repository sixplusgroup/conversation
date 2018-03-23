package finley.gmair.dao.impl;

import finley.gmair.dao.BaseDao;
import finley.gmair.dao.TextTemplateDao;
import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class TextTemplateDaoImpl extends BaseDao implements TextTemplateDao {
    private Logger logger = LoggerFactory.getLogger(TextTemplateDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(TextTemplate textTemplate) {
        ResultData result = new ResultData();
        textTemplate.setTemplateId(IDGenerator.generate("TTI"));
        try {
            sqlSession.insert("gmair.wechat.texttemplate.insert", textTemplate);
            result.setData(textTemplate);
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
            List<TextTemplate> list = sqlSession.selectList("gmair.wechat.texttemplate.query", condition);
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
    public ResultData update(TextTemplate textTemplate) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.texttemplate.update", textTemplate);
            result.setData(textTemplate);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}