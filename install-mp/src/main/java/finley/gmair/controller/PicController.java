package finley.gmair.controller;

import finley.gmair.model.installation.Pic;
import finley.gmair.service.PicService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource(value = "classpath:/install.properties")
public class PicController {
    @Autowired
    PicService picService;

    @Autowired
    TempFileMapService tempFileMapService;

    @Value("${RESOURCE_URL}")
    private String RESOURCE_URL;

    @Value("${STORAGE_PATH}")
    private String STORAGE_PATH;

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

        //actualPath does not contains file name but picPath does
        String actualPath = new StringBuffer(STORAGE_PATH)
                .append(File.separator)
                .append(new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .toString();
        String fileName = new StringBuffer(IDGenerator.generate("PIC"))
                .append(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')))
                .toString();
        String fileUrl = new StringBuffer(RESOURCE_URL)
                .append(File.separator)
                .append(fileName)
                .toString();
        String picPath = new StringBuffer(actualPath)
                .append(File.separator)
                .append(fileName)
                .toString();
        String memberPhone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //create the pic-saving directory and save the pic to disk.
        File directory = new File(actualPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            file.transferTo(new File(picPath));
        } catch (IOException e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
            return result;
        }

        //compute md5, save pic to install_pic, save temp url-path map to tempfile_location
        new Thread(() -> {
            solveMd5(fileUrl, picPath, memberPhone);
            tempFileMapService.createPicMap(fileUrl, actualPath, fileName);
        }).start();

        result.setData(fileUrl);
        return result;
    }

    private void solveMd5(String fileUrl, String picPath, String memberPhone) {
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
            picService.createPic(pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
