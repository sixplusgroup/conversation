package finley.gmair.controller;

import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.service.CorpMachineSubsService;
import finley.gmair.service.CorpProfileService;
import finley.gmair.service.MachineSummaryService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyy
 * @Description
 * @create 2020-06-30 11:29 下午
 */
@RestController
@RequestMapping("/openplatform/summary")
public class SummaryController {

    @Resource
    MachineSummaryService machineSummaryService;

    @Autowired
    private CorpProfileService corpProfileService;

    @Autowired
    private CorpMachineSubsService corpMachineSubsService;

    @GetMapping("/pm2_5")
    public ResultData queryWeekly(String appid, String qrcode) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(appid) || StringUtils.isEmpty(qrcode)) {
            result.setDescription("请提供正确的appid和qrcode");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }

        //检查appid和qrcode是否存在订阅关系
        if (!prerequisities(appid, qrcode)) {
            result.setDescription("请提供正确的appid和qrcode");
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }

        return machineSummaryService.getWeeklyPM25(qrcode);
    }

    /**
     * 判断appid和qrcode是否存在订阅关系
     *
     * @param appid
     * @param qrcode
     * @return
     */
    private boolean prerequisities(String appid, String qrcode) {
        ResultData result = new ResultData();
        //判断appid是否合法
        Map<String, Object> condition = new HashMap<>();
        condition.put("appid", appid);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("检查appid合法性失败");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供正确的appid");
            return false;
        }
        CorpProfile corpProfile = ((List<CorpProfile>) response.getData()).get(0);
        String corpId = corpProfile.getProfileId();
        //检查该appid和qrcode是否存在订阅关系
        Map<String, Object> con = new HashMap<>();
        con.put("corpId", corpId);
        con.put("qrcode", qrcode);
        con.put("blockFlag", false);
        response = corpMachineSubsService.fetch(con);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询与该设备的订阅关系失败");
            return false;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未订阅该设备");
            return false;
        }
        return true;
    }

}
