package finley.gmair.controller;

import finley.gmair.model.assemble.Snapshot;
import finley.gmair.model.wechat.AccessToken;
import finley.gmair.service.FileMapService;
import finley.gmair.service.SnapshotService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.service.WechatService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/assemble/snapshot")
@PropertySource(value = "classpath:/assemble.properties")
public class SnapshotController {

    @Autowired
    private FileMapService fileMapService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private WechatService wechatService;

    @Value("${RESOURCE_URL}")
    private String RESOURCE_URL;

    @Value("${STORAGE_PATH}")
    private String STORAGE_PATH;

    //工人提交图片对应微信服务器上的url和条形码时触发,创建snapshot表单
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(String codeValue, String mediaId) {
        ResultData result = new ResultData();
        //check whether input is empty
        if (StringUtils.isEmpty(codeValue) || StringUtils.isEmpty(mediaId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        //actualPath does not contains file name but picPath does
        String actualPath = new StringBuffer(STORAGE_PATH)
                .append(File.separator)
                .append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .toString();
        String filename = IDGenerator.generate("ZJP") + ".jpg";
        String fileUrl = new StringBuffer(RESOURCE_URL)
                .append(File.separator)
                .append(filename)
                .toString();
        ResultData response = wechatService.getToken();
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get access token");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the access token");
            return result;
        }
        String access_token = ((LinkedHashMap<String, String>) response.getData()).get("accessToken");
        //String access_token = "14_dQ2As1M6CMuf6D-5IIh8W0TRJKci3K6V4ugEmZI7YWLyaaCP1FV4Sy55CeOJJpGWmzMTASm0pVifL6BlLjxvDmEq-0u4KnTENCw6FtH6wExKQY_TTSYvzsvGe9wrf2l4sdTzkrGXE0SN4PNySYXaAEALRQ";
        new Thread(()->{
            downloadPic(actualPath,filename,mediaId,access_token);
        });
        response = fileMapService.create(fileUrl, actualPath, filename);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create file map");
            return result;
        }
        result.setDescription("success to create file map");
        return result;
    }
    //下载微信服务器上的图片
    //actualPath 本地文件夹名
    //filename 保存的文件名
    //media_id 微信服务器对资源的编号
    //token 微信公众号的token
    private ResultData downloadPic(String actualPath, String filename, String media_id, String token) {
        ResultData result = new ResultData();
        String url = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=" + token + "&media_id=" + media_id;
        try {
            URL address = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) address.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");
            connection.connect();
            InputStream is = connection.getInputStream();
            //路径处理
            File directory = new File(actualPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String completeName = new StringBuffer(actualPath).append(File.separator).append(filename).toString();
            File temp = new File(completeName);
            //开始下载
            FileOutputStream fileOutputStream = new FileOutputStream(temp);
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
            result.setData(filename);
        } catch (Exception e) {

        } finally {
            return result;
        }
    }


    //显示snapshot list
    @RequestMapping(method = RequestMethod.GET, value = "/fetch")
    public ResultData fetchSnapshot(String startTime, String endTime, String codeValue, String snapshotId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(startTime)) {
            condition.put("createTimeGTE", startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            condition.put("createTimeLT", endTime);
        }
        if (!StringUtils.isEmpty(codeValue)) {
            condition.put("codeValue", codeValue);
        }
        if (!StringUtils.isEmpty(snapshotId)) {
            condition.put("snapshotId", snapshotId);
        }
        ResultData response = snapshotService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch data from server");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find data by such condition");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch");
        return result;
    }




}