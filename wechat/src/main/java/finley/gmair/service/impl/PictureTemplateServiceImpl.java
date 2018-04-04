package finley.gmair.service.impl;

import finley.gmair.dao.PictureTemplateDao;
import finley.gmair.model.wechat.PictureTemplate;
import finley.gmair.service.PictureTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PictureTemplateServiceImpl implements PictureTemplateService {
    private Logger logger = LoggerFactory.getLogger(PictureTemplateServiceImpl.class);

    @Autowired
    private PictureTemplateDao pictureTemplateDao;

    @Override
    @Transactional
    public ResultData create(PictureTemplate template) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateDao.insert(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store picturetemplate to database");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = pictureTemplateDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No picturetemplate found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve picturetemplate information from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData modify(PictureTemplate template) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("templateId", template.getTemplateId());
        ResultData response = pictureTemplateDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No picturetemplate found from database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query picturetemplate information from database");
            return result;
        }
        response = pictureTemplateDao.update(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update picturetemplate to database");
        return result;
    }
}