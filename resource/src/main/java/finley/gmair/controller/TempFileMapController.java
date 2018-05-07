package finley.gmair.controller;


import finley.gmair.model.resource.FileMap;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/resource/tempfilemap")
public class TempFileMapController {
    @Autowired
    private TempFileMapService tempFileMapService;

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

    @RequestMapping(method = RequestMethod.POST, value = "/deletevalid")
    public ResultData deleteValidPicMapFromTempFileMap(String fileUrl) {
        ResultData result = new ResultData();
        String[] urls = fileUrl.split(",");
        Map<String, Object> condition = new HashMap<>();
        //delete the url
        for (int i = 0; i < urls.length; i++) {
            condition.clear();
            condition.put("fileUrl", urls[i]);
            condition.put("blockFlag", false);
            tempFileMapService.deleteTempFileMap(condition);
        }
        return result;
    }

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


}
