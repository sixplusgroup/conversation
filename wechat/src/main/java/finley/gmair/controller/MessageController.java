package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.WechatUser;
import finley.gmair.service.AccessTokenService;
import finley.gmair.service.WechatUserService;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
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
public class MessageController {

    @Autowired
    private AccessTokenController accessTokenController;

    @Value("${wechat_appid}")
    private String wechatAppId;

    @Value("${wechat_secret}")
    private String wechatSecret;

    @Value("${wechat_token}")
    private String wechatToken;

    @Value("${wechat_model4}")
    private String wechatmodel4;

    @Value("${wechat_tiaozhuan_appid}")
    private String wechattiaozhuanappid;

    @Value("${wechat_tiaozhuan_pagepath}")
    private String wechattiaozhuanpagepath;



    @GetMapping(value = "/confirmedMessage")
    public ResultData confirmMessage(String orderId) throws IOException {
//        //获得令牌
//        ResultData re = accessTokenController.getToken(wechatAppId);
//
//        String TemplateMessage_Url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
//
//        String token = ((List<AccessToken>)re.getData()).get(0).getAccessToken();
//
        //创建返回实体对象
        ResultData vo = new ResultData();
//        //获得新的token
//        String url=TemplateMessage_Url.replace("ACCESS_TOKEN", token);

        String postUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + wechatToken;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", "ojbJqw0LzsrTfhdSkalogPhPUaro");   // openid
        jsonObject.put("template_id", "AIemNnnzCP9sk6XlNH9J2n2uucfJFgaBhCfoudKnq3c");
        jsonObject.put("url", "http://www.baidu.com");

        JSONObject data = new JSONObject();
        JSONObject first = new JSONObject();
        first.put("value", "hello");
        first.put("color", "#173177");
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", "hello");
        keyword1.put("color", "#173177");
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", "hello");
        keyword2.put("color", "#173177");
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", "hello");
        keyword3.put("color", "#173177");
        JSONObject remark = new JSONObject();
        remark.put("value", "hello");
        remark.put("color", "#173177");

        data.put("first",first);
        data.put("keyword1",keyword1);
        data.put("keyword2",keyword2);
        data.put("keyword3",keyword3);
        data.put("remark",remark);

        jsonObject.put("data", data);

        String string = HttpClientUtils.sendPostJsonStr(postUrl, jsonObject.toJSONString());
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

    @GetMapping(value = "/returnMessage")
    public ResultData returnMessage(String orderId,String wechat,String activityName,String expectDate) throws IOException {
        //获得令牌
        ResultData re = accessTokenController.getToken(wechatAppId);

        String TemplateMessage_Url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

        String token = ((AccessToken)re.getData()).getAccessToken();

        //创建返回实体对象
        ResultData vo = new ResultData();
        //获得新的token
        String url=TemplateMessage_Url.replace("ACCESS_TOKEN", token);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", wechat);   // openid
        jsonObject.put("template_id",wechatmodel4);
//        jsonObject.put("url", "http://www.baidu.com");

        JSONObject miniprogram = new JSONObject();
        miniprogram.put("appid",wechattiaozhuanappid);
        miniprogram.put("pagepath",wechattiaozhuanpagepath+orderId);

        jsonObject.put("miniprogram",miniprogram);
        //今天日期
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dd = formatter.format(date);

        JSONObject data = new JSONObject();
        JSONObject first = new JSONObject();
        first.put("value", "您好，您租赁的甲醛检测设备将于今日到期。请按时寄回，感谢您的使用");
        first.put("color", "#173177");
        JSONObject keyword1 = new JSONObject();
        keyword1.put("value", activityName);
        keyword1.put("color", "#173177");
        JSONObject keyword2 = new JSONObject();
        keyword2.put("value", expectDate + "至" + dd);
        keyword2.put("color", "#173177");
        JSONObject keyword3 = new JSONObject();
        keyword3.put("value", dd);
        keyword3.put("color", "#173177");
        JSONObject remark = new JSONObject();
        remark.put("value", "详情请查看果麦检测小程序");
        remark.put("color", "#173177");

        data.put("first",first);
        data.put("keyword1",keyword1);
        data.put("keyword2",keyword2);
        data.put("keyword3",keyword3);
        data.put("remark",remark);

        jsonObject.put("data", data);

        String string = HttpClientUtils.sendPostJsonStr(url, jsonObject.toJSONString());
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

    @GetMapping(value = "/modelId")
    public ResultData modelId() throws IOException {
//        ResultData re = accessTokenController.getToken(wechatAppId);
//
//        JSONObject re_json = JSONObject.parseObject(re.getData().toString());
//        String token = re_json.getString("accessToken");
//
//        String data = re_json.getString("data");

        String postUrl = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=" + wechatToken;

        String string = HttpClientUtils.sendPost(postUrl);

        System.out.println(string);

        ResultData res = new ResultData();

        return res;
    }

    @GetMapping(value = "/token")
    public ResultData token() throws IOException {


        String postUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

        String url=postUrl.replace("APPID", wechatAppId);

        url=url.replace("APPSECRET", wechatSecret);

        String string = HttpClientUtils.sendPost(url);

        System.out.println(string);

        ResultData res = new ResultData();

        return res;
    }

}