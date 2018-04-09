package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import finley.gmair.dao.MessageTemplateDao;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.service.MessageTemplateService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    @Override
    public ResultData createTemplate(MessageTemplate template) {
        ResultData result = new ResultData();
        ResultData response = messageTemplateDao.insertMessageTemplate(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to create template with: ").append(JSON.toJSONString(template)).toString());
        }
        return result;
    }

    @Override
    public ResultData fetchTemplate(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = messageTemplateDao.queryMessageTemplate(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch the template");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No template found with: ").append(JSON.toJSONString(condition)).toString());
        }
        return result;
    }

    @Override
    public ResultData reviseTemplate(MessageTemplate template) {
        ResultData result = new ResultData();
        ResultData response = messageTemplateDao.updateMessageTemplate(template);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to revise the template with id: ").append(template.getTemplateId()).append(" to ").append(JSON.toJSONString(template)).toString());
        }
        return result;
    }
}
