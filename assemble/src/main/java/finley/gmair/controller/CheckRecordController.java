package finley.gmair.controller;

import finley.gmair.model.assemble.CheckRecord;
import finley.gmair.service.CheckRecordService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assemble/checkrecord")
public class CheckRecordController {
    @Autowired
    private CheckRecordService checkRecordService;

    @PostMapping("/create")
    public ResultData createCheckRecord(String snapshotId, boolean recordStatus) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(snapshotId) || StringUtils.isEmpty(recordStatus)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please provide the snapshotId and recordStatus");
            return result;
        }

        //check if the snapshotId exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("snapshotId", snapshotId);
        condition.put("blockFlag", false);
        ResultData response = checkRecordService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("exist snapshotId,don't create again");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the check record by snapshotId");
            return result;
        }

        //create the check record
        CheckRecord checkRecord = new CheckRecord(snapshotId, recordStatus);
        response = checkRecordService.create(checkRecord);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the check record.");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the check record.");
        return result;
    }

    @GetMapping("/fetch/bystatus")
    public ResultData fetchCheckRecordByStatus(boolean recordStatus){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("recordStatus",recordStatus);
        condition.put("blockFlag",false);
        ResultData response = checkRecordService.fetch(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch check record by record status");
            return result;
        }else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("not find any data by status");
            return result;
        }
        result.setData(response.getData());
        result.setDescription("success to fetch check record list");
        return result;
    }
}
