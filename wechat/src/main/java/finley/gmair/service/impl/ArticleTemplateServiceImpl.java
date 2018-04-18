package finley.gmair.service.impl;

import finley.gmair.dao.ArticleTemplateDao;
import finley.gmair.model.wechat.ArticleTemplate;
import finley.gmair.service.ArticleTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ArticleTemplateServiceImpl implements ArticleTemplateService {
    private Logger logger = LoggerFactory.getLogger(ArticleTemplateServiceImpl.class);

    @Autowired
    private ArticleTemplateDao articleTemplateDao;

    @Override
    @Transactional
    public ResultData create(ArticleTemplate template) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateDao.insert(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store articletemplate to database");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No articletemplate found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve articletemplate from database");
        }
        return result;
    }

    @Override
    public ResultData modify(ArticleTemplate template) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(template.getTemplateId())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Can't update articletemplate without templateId");
            return result;
        } else {
            condition.put("templateId", template.getTemplateId());
            ResultData response = articleTemplateDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No articletemplate found from database");
                return result;
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query articletemplate information from database");
                return result;
            }
            response = articleTemplateDao.update(template);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update articletemplate to database");
            return result;
        }
    }

    @Override
    public ResultData fetchArticleReply(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = articleTemplateDao.queryArticleReply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No articleReply found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve articleReply from database");
        }
        return result;
    }
}