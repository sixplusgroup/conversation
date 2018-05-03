package finley.gmair.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import finley.gmair.model.wechat.*;
import finley.gmair.service.*;
import finley.gmair.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@SpringBootApplication
@RestController
@ComponentScan({"finley.gmair.scheduler", "finley.gmair.service", "finley.gmair.dao", "finley.gmair.controller"})
public class WechatApplication {

    @Autowired
    private TextTemplateService textTemplateService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private AutoReplyService autoReplyService;

    @Autowired
    private PictureTemplateService pictureTemplateService;

    @Autowired
    private ArticleTemplateService articleTemplateService;


    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/wechat")
    public String check(HttpServletRequest request) {
        String signature = request.getParameter("signature");// 微信加密签名
        String timestamp = request.getParameter("timestamp");// 时间戳
        String nonce = request.getParameter("nonce");// 随机数
        String echostr = request.getParameter("echostr");//
        List<String> params = new ArrayList<>();
        params.add(WechatProperties.getValue("wechat_token"));
        params.add(timestamp);
        params.add(nonce);
        Collections.sort(params);
        String temp = params.get(0) + params.get(1) + params.get(2);
        if (Encryption.SHA1(temp).equals(signature)) {
            return echostr;
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/wechat", produces = "text/xml;charset=utf-8")
    public String handle(HttpServletRequest request) {
        try {
            ServletInputStream stream = request.getInputStream();
            String input = WechatUtil.inputStream2String(stream);

            int start = input.indexOf("<MsgType>");
            int end = input.indexOf("</MsgType>");

            String type = input.substring(start + "<MsgType>".length(), end).replace("<![CDATA[", "").replace("]]>", "");

            switch (type) {
                case "text":
                    XStream content = XStreamFactory.init(false);
                    content.alias("xml", TextInMessage.class);
                    final TextInMessage tmessage = (TextInMessage) content.fromXML(input);
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("messageType", "text");
                    condition.put("keyword", tmessage.getContent());
                    ResultData response = autoReplyService.fetch(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        AutoReply reply = ((List<AutoReply>) response.getData()).get(0);
                        if (reply.getTemplateId().startsWith("TTE")) {
                            //clear & set the new filter condition
                            condition.clear();
                            condition.put("templateId", reply.getTemplateId());
                            TextOutMessage result = initialize(textResponse(condition), tmessage);
                            content.alias("xml", TextOutMessage.class);
                            String xml = content.toXML(result);
                            return xml;
                        }
                        if (reply.getTemplateId().startsWith("PTI")) {
                            condition.clear();
                            condition.put("templateId", reply.getTemplateId());
                            WechatUtil.pushImage(WechatProperties.getAccessToken(), tmessage.getFromUserName(), pictureUrl(condition));
                        }
                    }
                    break;
                case "event":
                    content = XStreamFactory.init(false);
                    content.alias("xml", EventInMessage.class);
                    final EventInMessage emessage = (EventInMessage) content.fromXML(input);
                    Map<String, Object> map = new HashMap<>();
                    if (emessage.getEvent().equals("subscribe")) {
                        map.put("messageType", "event");
                        map.put("keyword", "subscribe");
                        TextOutMessage result = initialize(textResponse(map), emessage);
                        content.alias("xml", TextOutMessage.class);
                        String xml = content.toXML(result);
                        //start thread to store or update user information
                        new Thread(() -> {
                            String openId = emessage.getFromUserName();
                            String accessToken = WechatProperties.getAccessToken();
                            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
                            String resultStr = HttpDeal.getResponse(url);
                            JSONObject json = JSON.parseObject(resultStr);
                            WechatUser user = new WechatUser(openId, json);
                            map.clear();
                            map.put("wechatId", openId);
                            ResultData rd = new ResultData();
                            if (wechatUserService.existWechatUser(map)) {
                                rd = wechatUserService.modify(user);
                            } else {
                                rd = wechatUserService.create(user);
                            }
                        }).start();
                        return xml;
                    }
                    if (emessage.getEventKey().equals("gmair")) {
                        String openId = emessage.getFromUserName();
                        map.clear();
                        map.put("wechatId", openId);
                        ResultData rd = wechatUserService.fetch(map);
                        WechatUser WVo = ((List<WechatUser>) rd.getData()).get(0);
                        if (WVo != null && WVo.getWechatId() != null) {
                            //new machine don't finish, function of this condition can't achieve, return null
                            return "";
                        } else {
                            map.clear();
                            map.put("messageType", "event");
                            map.put("Keyword", "NoId");
                            TextOutMessage result = initialize(textResponse(map), emessage);
                            content.alias("xml", TextOutMessage.class);
                            String xml = content.toXML(result);
                            return xml;
                        }
                    }
                    break;
                default:
                    return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private TextOutMessage initialize(String content, InMessage message) {
        TextOutMessage result = new TextOutMessage();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(new Date().getTime());
        result.setContent(content);
        return result;
    }

    private String textResponse(Map<String, Object> condition) {
        ResultData response = textTemplateService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            TextTemplate template = ((List<TextTemplate>) response.getData()).get(0);
            String result = template.getResponse();
            return result;
        } else {
            return "";
        }
    }

    private String pictureUrl(Map<String, Object> condition) {
        ResultData rd = pictureTemplateService.fetch(condition);
        if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
            PictureTemplate template = ((List<PictureTemplate>) rd.getData()).get(0);
            String result = template.getPictureUrl();
            return result;
        } else {
            return "";
        }
    }
}
