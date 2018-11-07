package finley.gmair.controller;


import finley.gmair.model.resource.FileMap;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.FileUtil;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/resource/tempfilemap")
@PropertySource(value = "classpath:/resource.properties")
public class TempFileMapController {

    @Value("${RESOURCE_URL}")
    private String RESOURCE_URL;

    @Value("${STORAGE_PATH}")
    private String STORAGE_PATH;

    @Autowired
    private TempFileMapService tempFileMapService;

    //创建一个临时TempFileMap
    @RequestMapping(method = RequestMethod.POST, value = "/createpic")
    public ResultData createPicMap(String fileUrl, String actualPath, String fileName) {

        ResultData result = new ResultData();
        //save the tempFileMap
        FileMap tempFileMap = new FileMap(fileUrl, actualPath, fileName);
        ResultData response = tempFileMapService.createTempFileMap(tempFileMap);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the tempFileMap");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(fileUrl);
            result.setDescription("success to create the tempFileMap");
        }
        return result;
    }

    //从TempFileMap表中删除有效的Map
    @RequestMapping(method = RequestMethod.POST, value = "/deletevalid")
    public ResultData deleteValidPicMapFromTempFileMap(String fileUrl) {
        ResultData result = new ResultData();
        String[] urls = fileUrl.split(",");
        Map<String, Object> condition = new HashMap<>();
        //delete the url
        for (int i = 0; i < urls.length; i++) {
            condition.clear();
            condition.put("fileUrl", urls[i]);
            tempFileMapService.deleteTempFileMap(condition);
        }
        return result;
    }

    //通过url获取图片的本地存储路径,返回值包含图片本身的名字
    @RequestMapping(method = RequestMethod.GET, value = "/actualpath")
    public ResultData actualPath(String url) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("fileUrl", url);
        condition.put("blockFlag", false);
        ResultData response = tempFileMapService.fetchTempFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("url is invalid");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            String actualPath = ((List<FileMap>) response.getData()).get(0).getActualPath();
            String fileName = ((List<FileMap>) response.getData()).get(0).getFileName();
            result.setData(actualPath + File.separator + fileName);
            result.setDescription("success to get the actualPath");
        }
        return result;
    }

    //获取TempFileMap表中七天前的无效的Map,以便installation知道要删除哪些url记录
    @RequestMapping(method = RequestMethod.GET, value = "getinvalid")
    public ResultData getInvalidMap() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("dateFlag", true);
        condition.put("blockFlag", false);
        ResultData response = tempFileMapService.fetchTempFileMap(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get invalid map");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to get invalid map");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no map found");
        }
        return result;
    }

    //删除TempFileMap表中无效记录和对应的图片文件.
    @RequestMapping(method = RequestMethod.GET, value = "/deleteinvalid")
    public ResultData deleteInValidPicAndMap() {
        ResultData result = new ResultData();
        //删除大于7天的临时文件
        Map<String, Object> condition = new HashMap<>();
        condition.put("dateFlag", true);
        condition.put("blockFlag", false);
        List<FileMap> tempfilemapList = (List<FileMap>) (tempFileMapService.fetchTempFileMap(condition)).getData();
        if (tempfilemapList != null) {
            for (FileMap fm : tempfilemapList) {
                String actualPath = fm.getActualPath() + File.separator + fm.getFileName();
                System.out.println("start to delete " + actualPath);
                FileUtil.deleteFile(actualPath);
            }
        }

        //删除表中大于7天的临时数据表记录
        condition.clear();
        condition.put("dateFlag", true);
        tempFileMapService.deleteTempFileMap(condition);
        return result;
    }


    //上传图片
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public ResultData uploadPic(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = request.getFile("file");

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

        ResultData response = tempFileMapService.createTempFileMap(new FileMap(fileUrl, actualPath, fileName));
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(fileUrl);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to create temp file map");
            return result;
        }
        return result;
    }
}
