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


@RestController
@RequestMapping("/resource/tempfilemap")
public class TempFileMapController {
    @Autowired
    private TempFileMapService tempFileMapService;

    @RequestMapping(method = RequestMethod.POST, value="/createpic")
    public ResultData createPicMap(File file)
    {
        ResultData result = new ResultData();

        //check whether pic is empty
        if(!file.exists()){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("the file is empty");
            return result;
        }

        //create the the file
        String httpPort = "https://192.168.1.1:8011/";
        String fileUrl = httpPort+"InstallPic/"+file.getName();
        String actualPath = "/User/wjq/Desktop/uploadImg/";
        String fileName = file.getName();
        FileMap tempFileMap = new FileMap(fileUrl,actualPath,fileName);
        ResultData response = tempFileMapService.createTempFileMap(tempFileMap);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the tempFileMap");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }
}
