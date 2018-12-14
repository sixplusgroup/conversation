package finley.gmair.controller;

import com.netflix.discovery.CommonConstants;
import com.thoughtworks.xstream.XStream;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.model.wechat.Image;
import finley.gmair.model.wechat.PictureOutMessage;
import finley.gmair.service.AccessTokenService;
import finley.gmair.util.*;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wechat/picture")
public class PictureReplyController {

    @Autowired
    private AccessTokenService accessTokenService;

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
    public String upload2MediaId (MultipartFile multiFile) throws IOException{
        String accessToken = getAccessToken();
        if (StringUtils.isEmpty(accessToken)) {
            return new StringBuffer("Can't be applied with the openId: ").append(accessToken).toString();
        }
        File file = multi2file(multiFile);
        String result = WechatUtil.uploadImage(accessToken, file);
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

    private File multi2file(MultipartFile multiFile) throws IOException {
        File file = new File(multiFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(multiFile.getBytes());
        fos.close();
        return file;
    }

    private String getAccessToken() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = accessTokenService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            return null;
        }
        AccessToken token = ((List<AccessToken>) response.getData()).get(0);
        return token.getAccessToken();
    }
}
