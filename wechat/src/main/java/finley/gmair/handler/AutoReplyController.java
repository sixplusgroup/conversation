package finley.gmair.controller;

import finley.gmair.model.wechat.AutoReply;
import finley.gmair.service.AutoReplyService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/autoreply")
public class AutoReplyController {
    private Logger logger = LoggerFactory.getLogger(AutoReplyController.class);

    @Autowired
    private AutoReplyService autoReplyService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData create(AutoReply reply) {
        ResultData result = new ResultData();
        ResultData response = autoReplyService.create(reply);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResultData query() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = autoReplyService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(response.getDescription());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResultData update(AutoReply reply) {
        ResultData result = new ResultData();
        ResultData response = autoReplyService.modify(reply);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }
}