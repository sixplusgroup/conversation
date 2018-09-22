package finley.gmair.service.impl;

import finley.gmair.dao.MessageDao;
import finley.gmair.dao.ReceiveMessageDao;
import finley.gmair.model.message.TextMessage;
import finley.gmair.service.MessageService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageDao messageDao;

    @Autowired
    private ReceiveMessageDao receiveMessageDao;

    @Override
    @Transactional
    public ResultData createTextMessage(TextMessage message) {
        ResultData result = new ResultData();
        ResultData response = messageDao.insert(message);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
            result.setDescription("Fail to save text message to database");
        }
        return result;
    }

    @Override
    public ResultData fetchTextMessage(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = messageDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription("Fail to fetch text message from database");
        } else {
            List<TextMessage> list = (List<TextMessage>) response.getData();
            result.setData(list);
        }
        return result;
    }

    @Override
    public ResultData createReceiveMessage(TextMessage message) {
        ResultData result = new ResultData();
        ResultData response = receiveMessageDao.insert(message);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else {
            result.setDescription(response.getDescription());
            result.setDescription("Fail to save text message uploaded to database");
        }
        return result;
    }

    @Override
    public ResultData fetchReceiveMessage(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = receiveMessageDao.query(condition);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setDescription("Fail to fetch received text message from database");
        } else {
            List<TextMessage> list = (List<TextMessage>) response.getData();
            result.setData(list);
        }
        return result;
    }
}
