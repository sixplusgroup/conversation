package finley.gmair.controller;

import finley.gmair.model.installation.Pic;
import finley.gmair.service.PicService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/install-mp/pic")
public class PicController {
    @Autowired
    PicService picService;

    @Autowired
    TempFileMapService tempFileMapService;

    //工人上传一张图片时触发
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData upload(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = request.getFile("fileName");

        //check the file not empty.
        try {
            if (file == null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("file empty");
                return result;
            }
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //actualPath
        String basePath = File.separator + "Users" + File.separator + "wjq" + File.separator + "desktop" + File.separator + "uploadIMG";
        String time = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
        String actualPath = basePath + File.separator + time;
        StringBuilder builder = new StringBuilder(actualPath);

        //according to the actualPath,create a directory.
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //fileName
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String key = IDGenerator.generate("PIC");
        String fileName = key + suffix;

        //fileUrl
        String fileUrl = "https://192.168.1.1:8011" + File.separator + "InstallPic" + File.separator + fileName;

        //transfer to the disk
        File temp = new File(builder.append(File.separator).append(fileName).toString());
        try {
            file.transferTo(temp);
            result.setData(fileUrl);
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //save the installation fileUrl
        String picPath = actualPath + File.separator + fileName;
        File picFile = new File(picPath);
        if (picFile.exists() == false) {
            return result;
        }

        //get memberPhone and save pic information
        //String memberPhone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String memberPhone = "123";
        Pic pic = new Pic(fileUrl, memberPhone);
        try {
            String picMd5 = DigestUtils.md5Hex(new FileInputStream(picPath));
            pic.setPicMd5(picMd5);
            //compute copyFlag
            Map<String, Object> condition = new HashMap<>();
            condition.put("picMd5", picMd5);
            condition.put("blockFlag", false);
            ResultData response = picService.fetchPic(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK)
                pic.setCopyFlag(true);
            else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL)
                pic.setCopyFlag(false);
            //保存
            picService.createPic(pic);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }


        //save the temp fileUrl-actualPath map
        ResultData response = tempFileMapService.createPicMap(fileUrl, actualPath, fileName);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("success to save tempFilemap");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to save tempFilemap");
        }
        result.setData(fileUrl);
        return result;
    }
}
