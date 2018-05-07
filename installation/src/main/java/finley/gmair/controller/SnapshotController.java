package finley.gmair.controller;

import finley.gmair.form.installation.SnapshotForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.Member;
import finley.gmair.model.installation.Snapshot;
import finley.gmair.model.resource.FileMap;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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

    @Autowired
    private MemberService memberService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private FileMapService fileMapService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(SnapshotForm form) {
        ResultData result = new ResultData();

        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String picPath = form.getPicPath().trim();

        //check whether input is empty
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(wechatId) || StringUtils.isEmpty(picPath)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        //according to the qrcode,find the assignId.
        String assignId = "";
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            assignId = ((List<Assign>) response.getData()).get(0).getAssignId();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the qrcode");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        //according to the wechatId,find the memberPhone.
        String memberPhone = "";
        condition = new HashMap<>();
        condition.put("wechatId", wechatId);
        condition.put("blockFlag", false);
        response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            memberPhone = ((List<Member>) response.getData()).get(0).getMemberPhone();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the wechat");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later.");
            return result;
        }

        //create the Snapshot
        Snapshot snapshot = new Snapshot(assignId, qrcode, wechatId, memberPhone, picPath);
        response = snapshotService.createSnapshot(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to create the snapshot");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the snapshot");
            return result;
        }

        //new a thread to update the assign status.
        condition.clear();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        final Map<String,Object> condition1 = condition;
        new Thread(()->{
            final ResultData response1 = assignService.fetchAssign(condition1);
            if (response1.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Assign assign = ((List<Assign>) response1.getData()).get(0);
                assign.setAssignStatus(AssignStatus.FINISHED);
                assignService.updateAssign(assign);
            }
        }).start();

        //new a thread to save the fileUrl-actualPath map
        new Thread(() -> {
            fileMapService.createPicMap(picPath);
            tempFileMapService.deleteValidPicMapFromTempFileMap(picPath);
        }).start();

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = snapshotService.fetchSnapshot(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to snapshot info");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No snapshot info at the moment");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to snapshot info");
        }
        return result;
    }


}
