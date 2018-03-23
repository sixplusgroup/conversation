package finley.gmair.dao.impl;

import finley.gmair.dao.ArticleTemplateDao;
import finley.gmair.dao.BaseDao;
import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class ArticleTemplateDaoImpl extends BaseDao implements ArticleTemplateDao {
    private Logger logger = LoggerFactory.getLogger(ArticleTemplateDaoImpl.class);

    @Override
    @Transactional
    public ResultData insert(ArticleTemplate articleTemplate) {
        ResultData result = new ResultData();
        articleTemplate.setTemplateId(IDGenerator.generate("ATI"));
        try {
            sqlSession.insert("gmair.wechat.articletemplate.insert", articleTemplate);
            result.setData(articleTemplate);
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
            List<ArticleTemplate> list = sqlSession.selectList("gmair.wechat.articletemplate.query", condition);
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
    public ResultData update(ArticleTemplate articleTemplate) {
        ResultData result = new ResultData();
        try {
            sqlSession.update("gmair.wechat.articletemplate.update", articleTemplate);
            result.setData(articleTemplate);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }
}