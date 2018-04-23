package finley.gmair.controller;

import finley.gmair.form.installation.PicCollectForm;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.service.MemberService;
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
@RequestMapping("/installation/snapshot")
public class SnapshotController {
    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private MemberService memberService;

    @RequestMapping("/create")
    public ResultData create(PicCollectForm form){
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
        if(StringUtils.isEmpty(form.getCheckList())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the check list pic");
            return result;
        }
        if(StringUtils.isEmpty(form.getHoleDirection())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the hole direction pic");
            return result;
        }
        if(StringUtils.isEmpty(form.getIndoorHole())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the indoor hole pic");
            return result;
        }
        if(StringUtils.isEmpty(form.getOutdoorHole())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the outdoor hole pic");
            return result;
        }
        if(StringUtils.isEmpty(form.getIndoorPreAir())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the indoor pre air pic");
            return result;
        }
        if(StringUtils.isEmpty(form.getIndoorPostAir())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the indoor post air pic");
            return result;
        }

        //build the Snapshot Model
        String assignId = form.getAssignId().trim();
        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String checkList = form.getCheckList().trim();
        String indoorHole = form.getIndoorHole().trim();
        String outdoorHole = form.getOutdoorHole().trim();
        String indoorPreAir = form.getIndoorPreAir().trim();
        String indoorPostAir = form.getIndoorPostAir().trim();
        String holeDirection = form.getHoleDirection().trim();
        Snapshot snapshot = new Snapshot(assignId,qrcode,wechatId,memberPhone,checkList,indoorHole,outdoorHole,indoorPreAir,indoorPostAir,holeDirection);

        //check whether the memberPhone is right.
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("SnapshotController: Member with phone ").append(memberPhone).append(" is not exist").toString());
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to fetch the member");
        }

        //create the the Snapshot
        response = snapshotService.createSnapshot(snapshot);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the snapshot");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        result.setDescription("Success to create the team");
        return result;

        //download the pic.
    }

    @GetMapping
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
