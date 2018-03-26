package gmair.finley.controller;


import finley.gmair.model.order.SetupProvider;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import gmair.finley.service.SetupProviderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SetupProviderController {

    private Logger logger = LoggerFactory.getLogger(SetupProvider.class);

    @Autowired
    private SetupProviderService setupProviderService;

    @RequestMapping(method = RequestMethod.GET, value = "/setupProvider/list")
    public ResultData missionChannelList() {
        ResultData result = new ResultData();
        Map<String, Object> map = new HashMap<>();
        map.put("blockFlag", false);
        ResultData response = setupProviderService.fetchMissionChannel(map);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setupProvider/create")
    public ResultData create(SetupProvider missionChannel) {
        ResultData result = new ResultData();
        ResultData response = setupProviderService.create(missionChannel);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务器忙，请稍后再试!");
        } else {
            result.setData(response.getData());
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setupProvider/update")
    public ResultData updateMissionChannel(SetupProvider missionChannel) {
        ResultData result = new ResultData();
        ResultData response = setupProviderService.modifyMissionChannel(missionChannel);
        result.setResponseCode(response.getResponseCode());
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试!");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/setupProvider/delete/{channelId}")
    public ResultData deleteMissionChannel(@PathVariable String channelId) {

        ResultData result = setupProviderService.deleteMissionChannel(channelId);
        logger.info("delete missionChannel using channelId: " + channelId);
        return result;
    }
}
