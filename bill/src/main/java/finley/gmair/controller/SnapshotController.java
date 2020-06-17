package finley.gmair.controller;

import finley.gmair.model.bill.DealSnapshot;
import finley.gmair.service.SnapshotService;
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
@RequestMapping("/bill/snapshot")
public class SnapshotController {

    @Autowired
    private SnapshotService snapshotService;

    @PostMapping(value = "/create")
    public ResultData createSnapshot(double actualPrice, String billId, String channelId,
                                     String appId, String accountId, String mchId, String deviceInfo,
                                     String isSubscribe, String bankType, double orderPrice,String feeType,
                                     String transactionId, String timeEnd)
    {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(actualPrice) || StringUtils.isEmpty(billId) || StringUtils.isEmpty(channelId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure you fill all the required fields");
            return result;
        }

        DealSnapshot snapshot = new DealSnapshot(actualPrice, billId, channelId,
            appId, accountId, mchId, deviceInfo, isSubscribe, bankType, orderPrice,
                feeType, transactionId, timeEnd);
        ResultData response = snapshotService.createSnapshot(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to insert snapshot to database");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @PostMapping(value = "/delete")
    public ResultData deleteSnapshot(String snapshotId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(snapshotId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("please make sure you fill all the required fields");
            return result;
        }

        ResultData response = snapshotService.deleteSnapshot(snapshotId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to delete snapshot");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to delete snapshot");
        }
        return result;
    }

    @GetMapping(value = "/query")
    public ResultData getSnapshot() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = snapshotService.fetchSnapshot(condition);
        switch (response.getResponseCode()) {
            case RESPONSE_NULL:
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("No snapshot found");
                break;
            case RESPONSE_ERROR:
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Query error,try again later");
                break;
            case RESPONSE_OK:
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setData(response.getData());
                break;
        }
        return result;
    }

}
