package finley.gmair.service.impl;

import finley.gmair.dao.AutoReplyDao;
import finley.gmair.model.wechat.AutoReply;
import finley.gmair.service.AutoReplyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class AutoReplyServiceImpl implements AutoReplyService {

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
        if (StringUtils.isEmpty(reply.getReplyId())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Can't update autoreply without replyId");
            return result;
        } else {
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
}