package finley.gmair.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import finley.gmair.model.wechat.*;
import finley.gmair.service.*;
import finley.gmair.util.*;
import finley.gmair.vo.wechat.AutoReplyVo;
import finley.gmair.vo.wechat.PictureReplyVo;
import finley.gmair.vo.wechat.TextReplyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public String handle(HttpServletRequest request, HttpServletRequest response) {
        try {
            ServletInputStream stream = request.getInputStream();
            String input = WechatUtil.inputStream2String(stream);
            XStream content = XStreamFactory.init(false);
            content.alias("xml", InMessage.class);
            final InMessage message = (InMessage) content.fromXML(input);
            HttpSession session = request.getSession();
            session.setAttribute("openId", message.getFromUserName());
            switch (message.getMsgType()) {
                case "text":
                    content.alias("xml", TextOutMessage.class);
                    final TextInMessage textInMessage = (TextInMessage) content.fromXML(input);
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("messageType", "text");
                    condition.put("keyWord", textInMessage.getContent());
                    ResultData rs = autoReplyService.fetch(condition);
                    if (rs.getResponseCode() == ResponseCode.RESPONSE_OK) {
                        AutoReplyVo aVo = ((List<AutoReplyVo>)rs.getData()).get(0);
                        if (aVo.getTemplateId().contains("PTI")) {
                            condition.clear();
                            condition.put("templateId", aVo.getTemplateId());
                            WechatUtil.pushImage(WechatProperties.getAccessToken(), message.getFromUserName(), getPicUrl(condition));
                        }
                        if (aVo.getTemplateId().contains("TTI")) {
                            condition.clear();
                            condition.put("templateId", aVo.getTemplateId());
                            TextOutMessage result = initialize(getResponse(condition), textInMessage);
                            String xml = content.toXML(result);
                            return xml;
                        }
                        if (aVo.getTemplateId().contains("ATI")) {

                        }
                    }
                case "event":
                    content.alias("xml", TextOutMessage.class);
                    final EventInMessage eventInMessage = (EventInMessage) content.fromXML(input);
                    Map<String, Object> map = new HashMap<>();
                    if (eventInMessage.getEvent().equals("subscribe")) {
                        map.put("keyWord", "subscribe");
                        TextOutMessage result = initialize(getResponse(map), eventInMessage);
                        String xml = content.toXML(result);
                        //start thread to store or update user information
                        new Thread(() -> {
                            String openId = eventInMessage.getFromUserName();
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
                    if (eventInMessage.getEventKey().equals("gmair")) {
                        String openId = eventInMessage.getFromUserName();
                        map.clear();
                        map.put("wechatId", openId);
                        ResultData rd = wechatUserService.fetch(map);
                        WechatUser WVo = ((List<WechatUser>) rd.getData()).get(0);
                        if (WVo != null && WVo.getWechatId() != null) {
                            //new machine don't finish, function of this condition can't achieve, return null
                            return "";
                        } else {
                            content.alias("xml", TextOutMessage.class);
                            map.clear();
                            map.put("KeyWord", "NoWechatId");
                            TextOutMessage result = initialize(getResponse(map), eventInMessage);
                            String xml = content.toXML(result);
                            return xml;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private TextOutMessage initialize(String content, AbstractInMessage message) {
        TextOutMessage result = new TextOutMessage();
        result.setFromUserName(message.getToUserName());
        result.setToUserName(message.getFromUserName());
        result.setCreateTime(new Date().getTime());
        result.setContent(content);
        return result;
    }

    private String getResponse(Map<String, Object> con) {
        ResultData rd = textTemplateService.fetchTextReply(con);
        if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
            TextReplyVo tVo = ((List<TextReplyVo>) rd.getData()).get(0);
            String result = tVo.getResponse();
            return result;
        } else {
            return "";
        }
    }

    private String getPicUrl(Map<String, Object> condition) {
        ResultData rd = pictureTemplateService.fetchPictureReply(condition);
        if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
            PictureReplyVo pVo = ((List<PictureReplyVo>) rd.getData()).get(0);
            String result = pVo.getPictureUrl();
            return result;
        } else {
            return "";
        }
    }

    private AutoReplyVo getArticle(Map<String, Object> condition) {
        ResultData rd = articleTemplateService.fetchArticleReply(condition);
        if (rd.getResponseCode() == ResponseCode.RESPONSE_OK) {
            AutoReplyVo aVo = ((List<AutoReplyVo>) rd.getData()).get(0);
            return aVo;
        } else {
            return null;
        }
    }
}
