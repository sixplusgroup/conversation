package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.response.im.ChatSession;
import finley.gmair.common.JingdongCommon;
import finley.gmair.common.TimeCommon;
import finley.gmair.dao.JdUserDOMapper;
import finley.gmair.dao.JdWaiterDOMapper;
import finley.gmair.dao.SessionMessageDOMapper;
import finley.gmair.dao.UserSessionDOMapper;
import finley.gmair.dto.chatlog.KafkaMessage;
import finley.gmair.dto.chatlog.KafkaSession;
import finley.gmair.jd.JingdongApiHelper;
import finley.gmair.jd.TimeUtil;
import finley.gmair.model.chatlog.Message;
import finley.gmair.model.chatlog.UserSession;
import finley.gmair.model.chatlog.usr.jd.JingdongUser;
import finley.gmair.model.chatlog.usr.jd.JingdongWaiter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.util.*;

//@Service
//@Log4j
@RestController
public class ChatLogFetchService {

    @Value("${kafka.topics.to-analysis}")
    String topicToAnalysis;

    @Autowired
    JingdongApiHelper jingdongApiHelper;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    JdUserDOMapper userDOMapper;

    @Resource
    JdWaiterDOMapper waiterDOMapper;

    @Resource
    UserSessionDOMapper userSessionDOMapper;

    @Resource
    SessionMessageDOMapper sessionMessageDOMapper;


    // 每日凌晨0点触发
    @Scheduled(cron = "0 15 2 * * ?")
    public void fetch() {
        Timestamp current = TimeUtil.getTodayZeroTimestamp();
        String endTime = TimeCommon.DATE_FORMAT.format(current.getTime());
        String beginTime = TimeCommon.DATE_FORMAT.format(current.getTime() - TimeCommon.ONE_DAY_MILLIS);

        jingdongApiHelper.querySessionList(beginTime, endTime)
                .ifPresent(chatSessionPage -> chatSessionPage
                        .getChatSessionList()
                        .stream()
                        .map(ChatSession::getCustomer)
                        .map(customer -> jingdongApiHelper.queryChatLog(beginTime, endTime, customer))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(jingdongApiHelper::formatChatLog)
                        .map(this::storeAndTransform)
                        .forEach(kafkaSession -> {
                            kafkaTemplate.send(topicToAnalysis, JSON.toJSONString(kafkaSession));
                            System.out.println("send kafka:" + kafkaSession.toString());
                        }));
//                        .forEach(System.out::println));
    }

    private KafkaSession storeAndTransform(JSONObject chatLog) {
        // store user
        String userName = chatLog.getString(JingdongCommon.KEY_USER_NAME);
        Integer usrId = userDOMapper.getUserIdByName(userName);
        if (usrId == null) {
            JingdongUser user = new JingdongUser().setName(userName);
            userDOMapper.insertUser(user);
            usrId = user.getId();
        }
        System.out.println("user id:" + usrId);
        // store waiter
        String waiterName = chatLog.getString(JingdongCommon.KEY_WAITER_NAME);
        Integer waiterId = waiterDOMapper.getWaiterIdByName(waiterName);
        if (waiterId == null) {
            JingdongWaiter waiter = new JingdongWaiter().setName(waiterName);
            waiterDOMapper.insertWaiter(waiter);
            waiterId = waiter.getId();
        }
        System.out.println("waiter id:" + waiterId);
        // store session
        String productId = chatLog.getString(JingdongCommon.KEY_PRODUCT_ID);
        String originalSessionId = chatLog.getString(JingdongCommon.KEY_SESSION_ID);
        UserSession session = new UserSession()
                .setUserId(usrId)
                .setWaiterId(waiterId)
                .setProductId(productId)
                .setOriginalSessionId(originalSessionId);
        userSessionDOMapper.insertUserSession(session);
        int sessionId = session.getId();
        System.out.println("session id:" + sessionId);


        // store message and transform to kafka message list
        JSONArray rawMessages = chatLog.getJSONArray(JingdongCommon.KEY_CHAT_MESSAGES);
        List<KafkaMessage> kafkaMessageList = storeAndTransformMessage(rawMessages, sessionId);

        // prepare for kafka
        return new KafkaSession(sessionId, kafkaMessageList);
    }

    private List<KafkaMessage> storeAndTransformMessage(JSONArray messages, int sessionId) {
        List<KafkaMessage> kafkaMessageList = new ArrayList<>();
        messages.stream()
                .map(Object::toString)
                .map(JSON::parseObject)
                .forEach(message -> {
                    String content = formatMessageContent(message.getString(JingdongCommon.KEY_CONTENT));
                    boolean isFromWaiter = message.getBoolean(JingdongCommon.KEY_IS_FROM_WAITER);
                    long timestamp = message.getLong(JingdongCommon.KEY_TIMESTAMP);
                    Message mes = new Message().setSessionId(sessionId)
                            .setFromWaiter(isFromWaiter)
                            .setContent(content)
                            .setTimestamp(timestamp);
                    sessionMessageDOMapper.insertSessionMessage(mes);
                    int messageId = mes.getId();
                    System.out.println("message id:" + messageId);
                    KafkaMessage item = new KafkaMessage().setMessageId(messageId).setContent(content);
                    kafkaMessageList.add(item);
                });
        return kafkaMessageList;
    }

    private String formatMessageContent(String content) {
        return content.replaceAll("[\r\n\t]", " ");
    }

    private void diskStore(String chatLog, String path) {
        try {
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(path, true)));
            out.write(chatLog + "\n");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    -------------------------------TEST------------------------------------
    @GetMapping("api/test")
    public boolean test() {
        String chatlog = "{\"jingdong_im_pop_chatlog_get_responce\":{\"code\":\"0\",\"ChatLogPage\":{\"totalRecord\":1,\"chatLogList\":[{\"content\":\"咨询订单号：236810113731    商品ID：10026084786191\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735195000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"欢迎光临果麦官方旗舰店~极致的产品才值得用户永久信赖 果麦科技依托于【南京大学】国家重点实验室，主营壁挂新风机、反渗透净水器等环保智能家电。【点击进入会员页面领取20元优惠券】：https://shopmember.m.jd.com/shopcard?venderId=752759&#x26;shopId=748610&#x26;venderType=2&#x26;channel=401 百万育儿大V的选择【大风量壁挂新风】：https://item.jd.com/24743154315.html 泡茶泡奶不用烧水【可直接出热水的净水器】：https://item.jd.com/10026084786192.html\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735195000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"开票在哪开\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735203000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"您好\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735204000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"您提供开票信息\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735219000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"我给您登记\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735221000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"电子票嘛\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735231000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"还是说寄过来\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735238000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"纸质发票的\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735254000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"邮寄\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735258000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"那我提供发票抬头和发票地址给您就行吧？\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735282000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"普票嘛\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735297000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"是的\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735299000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"普票专票都可以开的\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735307000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"那稍等 我确认下发票抬头\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735353000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"好的呢\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735359000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"名称：北京星奇畅想科技有限公司\\n税号：9111 0105 MA01 A25J 5U\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735391000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"好的\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735397000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"邮寄地址您发一下\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735446000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"发票寄到北京市海淀区北三环西路32号恒润国际大厦701室 黄育青 15902093370\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735469000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"开普票就行\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735480000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"好的呢\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735491000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"发票大概能到\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735544000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"大概多久能到\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735552000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"这个不确定\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735556000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"如果年前可以开出的话年前邮寄\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735563000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"年前来不及的话就年后哈\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735568000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21},{\"content\":\"行\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":false,\"time\":1642735571000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":11},{\"content\":\"#E-s21\",\"sid\":\"14a75177c5464636435c63b3da4efcd9\",\"waiterSend\":true,\"time\":1642735582000,\"skuId\":10026084786191,\"customer\":\"turtle081025\",\"waiter\":\"果麦洁洁\",\"channel\":21}]}}}\n";
        JSONObject json = jingdongApiHelper.formatChatLog(chatlog);
        System.out.println(json);
        KafkaSession kafkaSession = storeAndTransform(json);

        kafkaTemplate.send(topicToAnalysis, JSON.toJSONString(kafkaSession));
        System.out.println("send kafka:" + kafkaSession.toString());
        return true;
    }
}
