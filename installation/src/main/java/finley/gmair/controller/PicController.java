package finley.gmair.controller;

import finley.gmair.service.PicService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/pic")
public class PicController {
    @Autowired
    PicService picService;

    @Autowired
    TempFileMapService tempFileMapService;
    //处理上传图片的请求
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = request.getFile("fileName");

        //check the file not empty.
        try {
            if (file ==null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("file empty");
                return result;
            }
        }
        catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //actualPath
        String basePath =File.separator+"Users"+File.separator+"wjq"+File.separator+"desktop"+ File.separator+"uploadIMG";
        String time = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
        String actualPath = basePath+File.separator+time;
        StringBuilder builder = new StringBuilder(actualPath);

        //according to the actualPath,create a file.
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //fileName
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String key = IDGenerator.generate("PIC");
        String fileName = key + suffix;

        //fileUrl
        String fileUrl = "https://192.168.1.1:8011"+File.separator+"InstallPic"+File.separator+fileName;

        //save to the disk
        File temp = new File(builder.append(File.separator).append(fileName).toString());
        try {
            file.transferTo(temp);
            result.setData(fileUrl);
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //save the temp fileUrl-actualPath map
        ResultData response=tempFileMapService.createPicMap(fileUrl,actualPath,fileName);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setData(response.getData());
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to save tempFilemap");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save tempFilemap");
        }
        return result;
    }

    //从数据表install_pic中拉取重复图片的信息
    @RequestMapping(method = RequestMethod.GET, value="copy")
    public ResultData copy(){
        ResultData result = new ResultData();

        Map<String ,Object> condition = new HashMap<>();
        condition.put("copyFlag",true);
        condition.put("blockFlag",false);
        ResultData response = picService.fetchPic(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK)
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to duplicate pic");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no duplicate pic found");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later.");
        }
        return result;
    }


}
