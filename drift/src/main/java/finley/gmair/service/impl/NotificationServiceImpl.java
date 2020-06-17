package finley.gmair.service.impl;

import finley.gmair.dao.NotificationDao;
import finley.gmair.model.drift.Notification;
import finley.gmair.service.NotificationService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public ResultData createNotification(Notification notification){
        ResultData result = new ResultData();
        ResultData response = notificationDao.insert(notification);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert notification to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @Override
    public ResultData fetchNotification(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = notificationDao.query(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No notification found from database");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Fail to retrieve notification");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

    @Override
    public ResultData modifyNotification(Map<String, Object> condition){
        ResultData result = new ResultData();
        ResultData response = notificationDao.update(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update notification");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
