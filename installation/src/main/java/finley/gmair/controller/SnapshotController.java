package finley.gmair.controller;

import finley.gmair.form.installation.SnapshotForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.service.AssignService;
import finley.gmair.service.PicService;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installation/snapshot")
public class SnapshotController {
    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private PicService picService;

    @Autowired
    private AssignService assignService;

    @RequestMapping(method = RequestMethod.POST, value="/create")
    public ResultData create(SnapshotForm form){
        ResultData result = new ResultData();

        String assignId = form.getAssignId().trim();
        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String picPath = form.getPicPath().trim();

        //check whether input is empty
        if(StringUtils.isEmpty(assignId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the assignID");
            return result;
        }
        if(StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the qrcode");
            return result;
        }
        if(StringUtils.isEmpty(wechatId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the wechatId");
            return result;
        }
        if(StringUtils.isEmpty(memberPhone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the phone number");
            return result;
        }
        if(StringUtils.isEmpty(picPath)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the pic path");
            return result;
        }

        //check whether the assignId is exist.
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = snapshotService.fetchSnapshot(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription(new StringBuffer("Assign with id ").append(assignId).append(" is already exist").toString());
            return result;
        }

        //create the Snapshot
        Snapshot snapshot = new Snapshot(assignId,qrcode,wechatId,memberPhone,picPath);
        response = snapshotService.createSnapshot(snapshot);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Fail to create the snapshot");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the snapshot");

        //new a thread to handle the pic saving and repetition checking.
        new Thread(() -> {
            picService.savePic(memberPhone,picPath);
        }).start();

        //new a thread to update the assign status.
        condition.clear();
        condition.put("assignId",assignId);
        condition.put("blockFlag",false);
        response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            Assign assign = ((List<Assign>)response.getData()).get(0);
            assign.setAssignStatus(AssignStatus.FINISHED);
            new Thread(() -> {
                assignService.updateAssign(assign);
            }).start();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value="/list")
    public ResultData list()
    {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        ResultData response = snapshotService.fetchSnapshot(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to snapshot info");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No snapshot info at the moment");
        }
        else
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to snapshot info");
        }
        return result;
    }


}
