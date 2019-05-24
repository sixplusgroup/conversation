package finley.gmair.controller;

import finley.gmair.form.openplatform.CorpProfileForm;
import finley.gmair.model.openplatform.CorpProfile;
import finley.gmair.service.CorpProfileService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: CorpController
 * @Description: TODO
 * @Author fan
 * @Date 2019/5/7 2:01 PM
 */
@RestController
@RequestMapping("/openplatform/corp")
public class CorpController {
    private Logger logger = LoggerFactory.getLogger(CorpController.class);

    @Autowired
    private CorpProfileService corpProfileService;

    @PostMapping("/create")
    public ResultData create(CorpProfileForm form){
        ResultData result = new ResultData();
        if(org.springframework.util.StringUtils.isEmpty(form.getCorpName()) || org.springframework.util.StringUtils.isEmpty(form.getCorpEmail())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all the required information is provided.");
            return result;
        }
        String corpName = form.getCorpName().trim();
        String corpEmail = form.getCorpEmail().trim();
        Map<String,Object> condition = new HashMap<>();
        condition.put("corpName", corpName);
        condition.put("corpEmail", corpEmail);
        condition.put("blockFlag", false);
        ResultData response = corpProfileService.fetch(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("企业名：" + form.getCorpName() + "的用户已存在");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建用户失败，请稍后尝试");
            return result;
        }
        CorpProfile corpProfile = new CorpProfile(corpName,corpEmail);
        response = corpProfileService.create(corpProfile);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            response.setDescription("创建用户成功");
        }else {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            response.setDescription("创建用户失败，请稍后尝试");
        }
        return result;
    }

    @GetMapping("/profile")
    public ResultData profile(String corpName, String corpEmail, String appid){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(corpName) && StringUtils.isEmpty(corpEmail) && StringUtils.isEmpty(appid)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供信息进行查找");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(appid)){
            condition.put("profileId",appid);
        }
        if(!StringUtils.isEmpty(corpName)){
            condition.put("corpName",corpName);
        }
        if(!StringUtils.isEmpty(corpEmail)){
            condition.put("corpEmail",corpEmail);
        }
        condition.put("blockFlag",false);
        ResultData response = corpProfileService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到相关企业的信息");
            return result;
        }
        result.setData(((List) response.getData()).get(0));
        return result;
    }
}
