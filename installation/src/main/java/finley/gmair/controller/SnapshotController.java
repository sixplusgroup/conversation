package finley.gmair.controller;

import finley.gmair.form.installation.SnapshotForm;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.service.PicService;
import finley.gmair.service.SnapshotService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/snapshot")
public class SnapshotController {
    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private PicService picService;


    @RequestMapping("/create")
    public ResultData create(SnapshotForm form){
        ResultData result = new ResultData();

        //check empty input
        if(StringUtils.isEmpty(form.getAssignId())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the assignID");
            return result;
        }
        if(StringUtils.isEmpty(form.getQrcode())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the qrcode");
            return result;
        }
        if(StringUtils.isEmpty(form.getWechatId())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the wechatId");
            return result;
        }
        if(StringUtils.isEmpty(form.getMemberPhone())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the phone number");
            return result;
        }
        if(StringUtils.isEmpty(form.getPicPath())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the pic path");
            return result;
        }

        //build the Snapshot Model
        String assignId = form.getAssignId().trim();
        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String picPath = form.getPicPath().trim();
        Snapshot snapshot = new Snapshot(assignId,qrcode,wechatId,memberPhone,picPath);

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
        response = snapshotService.createSnapshot(snapshot);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Fail to create the snapshot");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the snapshot");


        //new a thread to handle the pic saving work
        new Thread(() -> {
            System.out.println("start the savePic thread");
            picService.savePic(memberPhone,picPath);
        }).start();
        return result;

    }

    @RequestMapping("/list")
    public ResultData list()
    {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFalg",false);
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
