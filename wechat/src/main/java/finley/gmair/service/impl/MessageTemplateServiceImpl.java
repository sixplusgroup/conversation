package finley.gmair.service.impl;

import finley.gmair.dao.AccessTokenDao;
import finley.gmair.dao.MessageTemplateDao;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.MessageTemplate;
import finley.gmair.service.AccessTokenService;
import finley.gmair.service.MessageTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    @Transactional
    public ResultData create(MessageTemplate message) {
        ResultData result = new ResultData();
        ResultData response = messageTemplateDao.insert(message);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to store message template to database");
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = messageTemplateDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No accessToken found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve message template from database");
        }
        return result;
    }

    @Override
    public ResultData renew(MessageTemplate message) {
        ResultData result = new ResultData();
        //create a new record in database
        ResultData response = messageTemplateDao.insert(message);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to insert the message template : ").append(message.getTemplateId()).toString());
        }
        return result;
    }
}