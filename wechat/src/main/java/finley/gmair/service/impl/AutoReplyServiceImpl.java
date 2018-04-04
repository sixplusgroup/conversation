package finley.gmair.service.impl;

import finley.gmair.dao.AutoReplyDao;
import finley.gmair.model.wechat.AutoReply;
import finley.gmair.service.AutoReplyService;
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
public class AutoReplyServiceImpl implements AutoReplyService {
    private Logger logger = LoggerFactory.getLogger(AutoReplyServiceImpl.class);

    @Autowired
    private AutoReplyDao autoReplyDao;

    @Override
    @Transactional
    public ResultData create(AutoReply reply) {
        ResultData result = new ResultData();
        ResultData response = autoReplyDao.insert(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store autoreply to database");
        }
        return result;
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = autoReplyDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No autoreply found from database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to retrieve autoreply from database");
        }
        return result;
    }

    @Override
    public ResultData modify(AutoReply reply) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("replyId", reply.getReplyId());
        ResultData response = autoReplyDao.query(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No autoreply found from database");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query autoreply information from database");
            return result;
        }
        response = autoReplyDao.update(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        result.setDescription("Fail to update autoreply to database");
        return result;
    }
}