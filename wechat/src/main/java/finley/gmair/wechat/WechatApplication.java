package finley.gmair.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import finley.gmair.model.consumer.Consumer;
import finley.gmair.model.machine.ConsumerQRcodeBind;
import finley.gmair.model.wechat.*;
import finley.gmair.model.wechat.Article;
import finley.gmair.service.*;
import finley.gmair.util.*;
import finley.gmair.vo.wechat.PictureReplyVo;
import finley.gmair.vo.wechat.TextReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@EnableFeignClients({"finley.gmair.service"})
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

    @Autowired
    private AuthConsumerService authConsumerService;

    @Autowired
    private MachineService machineService;


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
                            PictureOutMessage result = init(pictureUrl(condition), tmessage);
                            content.alias("xml", PictureOutMessage.class);
                            String xml = content.toXML(result);
                            return xml;
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
                    if (emessage.getEvent().equals("CLICK") && emessage.getEventKey().equals("gmair")) {
                        try {
                            String openId = emessage.getFromUserName();
                            ResultData data = authConsumerService.findConsumer(openId);
                            if (data.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                Consumer consumerVo = ((List<Consumer>) data.getData()).get(0);
                                data = machineService.findMachineList(consumerVo.getConsumerId());
                                if (data.getResponseCode() == ResponseCode.RESPONSE_OK) {
                                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(data.getData()));
                                    List<String> qrCodes = new ArrayList<>();
                                    for (Object item : array) {

                                    }
                                    List<Article> list = new ArrayList<>();
                                    Article article = new Article();
                                    article.setTitle("果麦新风");
                                    article.setPicUrl("http://commander.gmair.net/reception/www/img/logo_blue.png");
                                    article.setUrl(new StringBuffer("https://reception.gmair.net/machine/list").toString());
                                    article.setDescription("温度、湿度、风量、辅热……");
                                    list.add(article);
                                    ArticleOutMessage result = initial(list, emessage);
                                    content.alias("xml", ArticleOutMessage.class);
                                    content.alias("item", Article.class);
                                    String xml = content.toXML(result);
                                    return xml;
                                }
                                if (data.getResponseCode() == ResponseCode.RESPONSE_NULL) {

                                }
                            }
                        }catch(Exception e){
                                e.printStackTrace();
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

    private PictureOutMessage init(String mediaId, InMessage message) {
        PictureOutMessage result = new PictureOutMessage();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(new Date().getTime());
        result.setMediaId(mediaId);
        return result;
    }

    private ArticleOutMessage initial(List<Article> list, InMessage message) {
        ArticleOutMessage result = new ArticleOutMessage();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(new Date().getTime());
        result.setArticles(list);
        result.setArticleCount(list.size());
        return result;
    }

    private String textResponse(Map<String, Object> condition) {
        ResultData response = textTemplateService.fetchTextReply(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            TextReplyVo vo = ((List<TextReplyVo>) response.getData()).get(0);
            String result = vo.getResponse();
            return result;
        } else {
            return "";
        }
    }

    private String pictureUrl(Map<String, Object> condition) {
        ResultData rd = pictureTemplateService.fetchPictureReply(condition);
        if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
            PictureReplyVo vo = ((List<PictureReplyVo>) rd.getData()).get(0);
            String result = vo.getPictureUrl();
            return result;
        } else {
            return "";
        }
    }
}
