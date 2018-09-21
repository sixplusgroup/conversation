package finley.gmair.controller;

import finley.gmair.model.assemble.Snapshot;
import finley.gmair.service.FileMapService;
import finley.gmair.service.SnapshotService;
import finley.gmair.service.TempFileMapService;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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

    @Value("${RESOURCE_URL}")
    private String RESOURCE_URL;

    @Value("${STORAGE_PATH}")
    private String STORAGE_PATH;

    //工人上传一张图片时触发
    @RequestMapping(method = RequestMethod.POST, value = "/pic/upload")
    public ResultData uploadPic(MultipartHttpServletRequest request) {
        ResultData result = new ResultData();
        MultipartFile file = request.getFile("fileName");
        //check the file not empty.
        try {
            if (file == null || file.getBytes().length == 0) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("empty file");
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
        String fileName = new StringBuffer(IDGenerator.generate("KKK"))
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
            result.setDescription("error happen when save pic to disk");
            return result;
        }

        //save temp url-path map to tempfile_location
        ResultData response = tempFileMapService.createPicMap(fileUrl, actualPath, fileName);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to save temp file");
            return result;
        }
        result.setData(fileUrl);
        return result;
    }

    //工人提交图片对应url和条形码时触发,创建snapshot表单
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData createSnapshot(String codeValue, String snapshotPath) {
        ResultData result = new ResultData();

        //check whether input is empty
        if (StringUtils.isEmpty(codeValue) || StringUtils.isEmpty(snapshotPath)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        //new a thread to save information
        new Thread(() -> {
            try {
                fileMapService.createPicMap(snapshotPath);                               //修改filemap表
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                tempFileMapService.deleteValidPicMapFromTempFileMap(snapshotPath);       //修改tempfilemap表
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        //check if the codeValue exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", codeValue);
        condition.put("blockFlag", false);
        ResultData response = snapshotService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch codevalue");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("exist codeValue,do not need to create again");
            return result;
        }

        //create the Snapshot
        Snapshot snapshot = new Snapshot(codeValue, snapshotPath);
        response = snapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the snapshot.");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the snapshot.");
        return result;
    }

    //显示snapshot list
    @RequestMapping(method = RequestMethod.GET, value = "/fetch")
    public ResultData fetchSnapshot(String startTime, String endTime, String codeValue) {
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
