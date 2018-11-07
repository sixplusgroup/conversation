package finley.gmair.controller;

import finley.gmair.model.installation.MachinePic;
import finley.gmair.service.FileMapService;
import finley.gmair.service.MachinePicService;
import finley.gmair.service.MachineService;
import finley.gmair.service.TempFileMapService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/machine/pic")
public class MachinePicController {

    @Autowired
    private MachinePicService machinePicService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private FileMapService fileMapService;

    @Autowired
    private MachineService machineService;

    @Transactional
    @PostMapping("/create")
    public ResultData createMachinePic(String codeValue, String picUrl1, String picUrl2, String picUrl3) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(codeValue)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the codeValue");
            return result;
        }
        if (StringUtils.isEmpty(picUrl1) && StringUtils.isEmpty(picUrl2) && StringUtils.isEmpty(picUrl3)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide at least one pic");
            return result;
        }

        //check qrcode exist
        ResultData response = machineService.checkQRcodeExist(codeValue);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find the qrcode ");
            return result;
        }

        //check qrcode hasn't upload pic
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        response = machinePicService.fetchMachinePic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("this qrcode has already uploaded pic before");
            return result;
        }

        //create machine pic
        if (!StringUtils.isEmpty(picUrl1)) {
            new Thread(() -> {
                clearResource(picUrl1);
            }).start();
            ResultData response1 = machinePicService.createMachinePic(new MachinePic(codeValue, picUrl1));
            if (response1.getResponseCode() == ResponseCode.RESPONSE_ERROR)
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }

        if (!StringUtils.isEmpty(picUrl2)) {
            new Thread(() -> {
                clearResource(picUrl2);
            }).start();
            ResultData response2 = machinePicService.createMachinePic(new MachinePic(codeValue, picUrl2));
            if (response2.getResponseCode() == ResponseCode.RESPONSE_ERROR)
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }

        if (!StringUtils.isEmpty(picUrl3)) {
            new Thread(() -> {
                clearResource(picUrl3);
            }).start();
            ResultData response3 = machinePicService.createMachinePic(new MachinePic(codeValue, picUrl3));
            if (response3.getResponseCode() == ResponseCode.RESPONSE_ERROR)
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
        }
        return result;
    }

    private void clearResource(String picUrl) {
        try {
            fileMapService.createPicMap(picUrl);                               //修改filemap表
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            tempFileMapService.deleteValidPicMapFromTempFileMap(picUrl);       //修改tempfilemap表
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/fetch")
    public ResultData fetchMachinePicByQRcode(String codeValue) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(codeValue)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the codeValue");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = machinePicService.fetchMachinePic(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
