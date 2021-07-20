package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.wechat.UserSession;
import finley.gmair.service.UserService;
import finley.gmair.util.Encryption;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: UserController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/1 5:00 PM
 */
@RestController
@RequestMapping("/auth/user")
@PropertySource("classpath:wechat.properties")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Value("${wechat_appid}")
    private String appid;

    @Value("${wechat_secret}")
    private String secret;

    @PostMapping("/openid")
    public ResultData openid(String code) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(code)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you have the code when query this method");
            return result;
        }
        //query the openid by code, via wechat appid & secret
        JSONObject info = WechatUtil.query(appid, secret, code);
        logger.info(JSON.toJSONString(info));
        if (StringUtils.isEmpty(info) || !info.containsKey("openid") || !info.containsKey("session_key")) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("根据code获取用户的信息失败");
            return result;
        }
        String openId = info.getString("openid");
        String sessionKey = info.getString("session_key");
        UserSession session = new UserSession(openId, sessionKey);
        ResultData response = userService.createSession(session);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setDescription("创建/更新用户session成功");
            result.setData(openId);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
        }
        return result;
    }

    @PostMapping("/decode/phone")
    public ResultData decode(String data, String iv, String openid) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(iv)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供待解密的用户信息和解密矩阵");
            return result;
        }
        //获取用户的session key
        Map<String, Object> condition = new HashMap<>();
        condition.put("openId", openid);
        condition.put("blockFlag", false);
        ResultData response = userService.fetchSession(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前无法解密用户的数据");
            return result;
        }
        UserSession session = ((List<UserSession>) response.getData()).get(0);
        try {
            byte[] info = Encryption.aesDecrypt(Base64.getDecoder().decode(data.getBytes()), Base64.getDecoder().decode(session.getSessionKey().getBytes()), Base64.getDecoder().decode(iv.getBytes()));
            data = new String(info);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("该信息无法解析");
            return result;
        }
        String phone = JSONObject.parseObject(data).getString("purePhoneNumber");
        result.setData(phone);
        return result;
    }
}
