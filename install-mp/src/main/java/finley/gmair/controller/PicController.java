package finley.gmair.controller;

import finley.gmair.model.installation.Pic;
import finley.gmair.service.PicService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public ResultData upload(HttpServletRequest request) {
        ResultData result = new ResultData();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        FileItem item;
        try {
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(request));
            item = items.get(0);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to process the request");
            return result;
        }

        //actualPath
        String basePath = new StringBuffer(File.separator).append("root").append(File.separator).append("Material").append(File.separator).append("install").toString();
        String time = (new SimpleDateFormat("yyyyMMdd")).format(new Date());
        StringBuilder builder = new StringBuilder(basePath).append(File.separator).append(time);

        //according to the actualPath,create a directory.
        File directory = new File(builder.toString());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //fileName
        String suffix = "png";
        String key = IDGenerator.generate("PIC");
        String filename = key + suffix;

        save(builder.toString(), filename, item);

        //fileUrl
        String fileUrl = "https://microservice.gmair.net/resource/filemap/pic" + File.separator + filename;

        //check if the file is already created
        String picPath = new StringBuffer(builder).append(File.separator).append(filename).toString();
        File picFile = new File(picPath);
        if (picFile.exists() == false) {
            return result;
        }

        //get memberPhone and save pic information
        String memberPhone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //String memberPhone = "123";
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
        ResultData response = tempFileMapService.createPicMap(fileUrl, builder.toString(), filename);
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

    private void save(String base, String filename, FileItem item) {
        File target = new File(new StringBuffer(base).append(File.separator).append(filename).toString());
        try {
            item.write(target);
        } catch (Exception e) {
            //log if any error when it failed to save the image file
        }
    }
}
