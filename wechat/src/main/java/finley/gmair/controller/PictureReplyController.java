package finley.gmair.controller;

import com.thoughtworks.xstream.XStream;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.Image;
import finley.gmair.model.wechat.PictureOutMessage;
import finley.gmair.service.AccessTokenService;
import finley.gmair.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wechat/picture")
@PropertySource("classpath:/wechat.properties")
public class PictureReplyController {

    private Logger logger = LoggerFactory.getLogger(PictureReplyController.class);

    @Autowired
    private AccessTokenService accessTokenService;

    @Value("${file_save_path}")
    private String fileSavePath;

    @Value("${wechat_appid}")
    private String wechatAppId;

    @PostMapping(value = "/upload/and/reply")
    public ResultData upload_reply(String openId, MultipartFile file) {
        ResultData result = new ResultData();
        logger.info("open id: " + openId + ", file: " + file.toString());
        if (StringUtils.isEmpty(openId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }
        try {
            String mediaId = upload2MediaId(file);
            logger.info("media id: " + mediaId);
            String accessToken = getAccessToken();
            logger.info("access_token: " + accessToken);
            boolean status = WechatUtil.pushImage(accessToken, openId, mediaId);
            result.setDescription("reply successfully");
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    @PostMapping(value = "/reply", produces = "text/xml;charset=utf-8")
    public String reply(String openId, String mediaId) {
        if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(mediaId)) {
            return new StringBuffer("can not be applied with the openId: ").append(openId)
                    .append(" and the mediaId: ").append(mediaId).toString();
        }
        XStream content = XStreamFactory.init(false);
        PictureOutMessage result = init(openId, mediaId);
        content.alias("xml", PictureOutMessage.class);
        content.alias("xml", Image.class);
        String xml = content.toXML(result);
        return xml;
    }

    @PostMapping(value = "/upload/get/mediaId")
    public String upload2MediaId(MultipartFile file) throws IOException {
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            return new StringBuffer("Can't be applied with the openId: ").append(accessToken).toString();
        }
        File f = multi2file(file);
        String result = WechatUtil.uploadImage(accessToken, f);
        f.delete();
        return result;
    }

    private PictureOutMessage init(String openId, String mediaId) {
        PictureOutMessage result = new PictureOutMessage();
        result.setFromUserName(WechatProperties.getValue("wechat_openId"));
        result.setToUserName(openId);
        result.setCreateTime(new Date().getTime());
        result.setImage(new Image(mediaId));
        return result;
    }

    private File multi2file(MultipartFile file) throws IOException {
        File f = new File(String.format("%s/%s.jpg", fileSavePath, IDGenerator.generate("PIC")));
        file.transferTo(f);
        return f;
    }

    private String getAccessToken() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("appId", wechatAppId);
        condition.put("blockFlag", false);
        ResultData response = accessTokenService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return null;
        }
        AccessToken token = (AccessToken) response.getData();
        return token.getAccessToken();
    }
}
