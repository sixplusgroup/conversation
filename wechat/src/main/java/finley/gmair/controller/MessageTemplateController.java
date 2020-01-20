package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.protocol.Message;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.MessageTemplate;
import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.AccessTokenService;
import finley.gmair.service.MessageTemplateService;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wechat/message")
@PropertySource("classpath:wechat.properties")
public class MessageTemplateController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    @Autowired
    private AccessTokenController accessTokenController;

    @Value("${wechat_appid}")
    private String wechatAppId;

    @Value("${wechat_secret}")
    private String wechatSecret;

    @Value("${wechat_token}")
    private String wechatToken;

    @Value("${wechat_tiaozhuan_appid}")
    private String wechattiaozhuanappid;

    @Value("${wechat_tiaozhuan_pagepath}")
    private String wechattiaozhuanpagepath;

    /**
     * 根据模板类别号获取模板id等信息
     * @param type
     * @return
     */
    @GetMapping(value="/query")
    public ResultData getTemplate(int type){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("messageType", type);
        condition.put("blockFlag", false);
        ResultData response = messageTemplateService.fetch(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No template found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error, please try again later");
                break;
        }
        return result;
    }

    /**
     * 根据模板号推送消息
     * @param type
     * @param json
     * @param orderId
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/sendMessage")
    public ResultData sendMessage(int type,String json,String orderId) throws IOException {
        //获得令牌
        ResultData re = accessTokenController.getToken(wechatAppId);

        String TemplateMessage_Url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

        String token = ((AccessToken)re.getData()).getAccessToken();

        //创建返回实体对象
        ResultData vo = new ResultData();
        //获得新的token
        String url=TemplateMessage_Url.replace("ACCESS_TOKEN", token);
        //获取模板id
        ResultData res = getTemplate(type);
        String templateId = ((MessageTemplate)res.getData()).getTemplateId();

        JSONObject js = JSONObject.parseObject(json);
        js.put("template_id",templateId);
//        jsonObject.put("url", "http://www.baidu.com");

        //设置跳转页面
        JSONObject miniprogram = new JSONObject();
        miniprogram.put("appid",wechattiaozhuanappid);
        miniprogram.put("pagepath",wechattiaozhuanpagepath+orderId);
        js.put("miniprogram",miniprogram);


        String string = HttpClientUtils.sendPostJsonStr(url, js.toJSONString());
        System.out.println(string);
        JSONObject result = JSON.parseObject(string);
        int errcode = result.getIntValue("errcode");
        if(errcode == 0){
            // 发送成功
            System.out.println("发送成功");
        } else {
            // 发送失败
            System.out.println("发送失败");
        }

        if (errcode == 0){
            vo.setResponseCode(ResponseCode.RESPONSE_OK);
            vo.setDescription("成功");
        }else{
            vo.setResponseCode(ResponseCode.RESPONSE_ERROR);
            vo.setDescription("失败");
        }
        return vo;

    }

    /**
     * remain to be completed
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/modelList")
    public ResultData modelList() throws IOException {
        ResultData re = accessTokenController.getToken(wechatAppId);

        JSONObject re_json = JSONObject.parseObject(re.getData().toString());
        String token = re_json.getString("accessToken");

        String data = re_json.getString("data");

        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + wechatToken;

        String string = HttpClientUtils.sendPost(postUrl);

        System.out.println(string);

        ResultData res = new ResultData();

        return res;
    }

    /**
     * remain to be completed
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/modelId")
    public ResultData modelId() throws IOException {

        String postUrl = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=" + wechatToken;

        String string = HttpClientUtils.sendPost(postUrl);

        System.out.println(string);

        ResultData res = new ResultData();

        return res;
    }



}