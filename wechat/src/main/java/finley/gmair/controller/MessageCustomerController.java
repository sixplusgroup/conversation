package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.util.HttpClientUtils;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author ：CK
 * @date ：Created in 2021/1/5 21:18
 * @description：
 */

@RestController
@RequestMapping("/wechat/message")
@PropertySource("classpath:wechat.properties")
public class MessageCustomerController {
    private Logger logger = LoggerFactory.getLogger(MessageCustomerController.class);

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
     * 推送每日天气消息
     *
     * @param content 发送内容
     * @param openId 用户的openid
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/send/weather")
    public ResultData sendWeatherMessage(String content, String openId) throws IOException {
        ResultData re = accessTokenController.getToken(wechatAppId);

        String TemplateMessage_Url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

        String token = ((AccessToken) re.getData()).getAccessToken();

        //创建返回实体对象
        ResultData vo = new ResultData();
        //获得新的token
        String url = TemplateMessage_Url.replace("ACCESS_TOKEN", token);
        //测试环境
//        String url = TemplateMessage_Url.replace("ACCESS_TOKEN", wechatToken);

        JSONObject js = new JSONObject();
        JSONObject contentObject = new JSONObject();
        contentObject.put("content",content);
        js.put("touser", openId);   // openid
        js.put("msgtype","text");
        js.put("text",contentObject);

        String string = HttpClientUtils.sendPostJsonStr(url, js.toJSONString());
        System.out.println(string);
        JSONObject result = JSON.parseObject(string);
        int errcode = result.getIntValue("errcode");
        if (errcode == 0) {
            // 发送成功
            System.out.println("发送成功");
        } else {
            // 发送失败
            System.out.println("发送失败");
        }

        if (errcode == 0) {
            vo.setResponseCode(ResponseCode.RESPONSE_OK);
            vo.setDescription("成功");
        } else {
            vo.setResponseCode(ResponseCode.RESPONSE_ERROR);
            vo.setDescription("失败");
        }
        return vo;
    }
}
