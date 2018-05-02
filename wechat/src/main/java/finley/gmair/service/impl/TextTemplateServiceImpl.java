package finley.gmair.service.impl;

import finley.gmair.dao.TextTemplateDao;
import finley.gmair.model.wechat.TextTemplate;
import finley.gmair.service.TextTemplateService;
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
public class TextTemplateServiceImpl implements TextTemplateService {
    private Logger logger = LoggerFactory.getLogger(TextTemplateServiceImpl.class);

    @Autowired
    private TextTemplateDao textTemplateDao;

    @Override
    @Transactional
    public ResultData create(TextTemplate template) {
        ResultData result = new ResultData();
        ResultData response = textTemplateDao.insert(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store texttemplate to database");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = textTemplateDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No texttemplate found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve texttemplate information from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData modify(TextTemplate template) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isEmpty(template.getTemplateId())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Can't update texttemplate without templateId");
            return result;
        } else {
            condition.put("templateId", template.getTemplateId());
            ResultData response = textTemplateDao.query(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No texttemplate found from database");
                return result;
            }
            if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to query texttemplate information from database");
                return result;
            }
            response = textTemplateDao.update(template);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                return result;
            }
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update texttemplate to database");
            return result;
        }
    }
}