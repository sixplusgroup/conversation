package finley.gmair.controller;

import finley.gmair.form.drift.DriftChannelForm;
import finley.gmair.model.drift.DriftChannel;
import finley.gmair.service.ChannelService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/drift/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping(value = "/create")
    public ResultData create(DriftChannelForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getChannelName())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return  result;
        }

        String channelName = form.getChannelName().trim();
        Map<String, Object> condition = new HashMap<>();
        condition.put("channelName", channelName);
        condition.put("blockFlag", false);
        ResultData response = channelService.fetchChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Channel: ").append(channelName).append(" already exist").toString());
            return result;
        }

        DriftChannel channel = new DriftChannel(channelName);
        response = channelService.createChannel(channel);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the drift channel");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @GetMapping(value = "/list")
    public ResultData getChannel() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = channelService.fetchChannel(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query channel");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No drift channel found");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
