package finley.gmair.controller;


import finley.gmair.form.installation.SnapshotForm;
import finley.gmair.model.installation.*;
import finley.gmair.service.*;
import finley.gmair.util.IPUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/install-mp/snapshot")
public class SnapshotController {
    @Autowired
    private SnapshotService snapshotService;

    @Autowired
    private AssignService assignService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TempFileMapService tempFileMapService;

    @Autowired
    private FileMapService fileMapService;

    @Autowired
    private SnapshotLocService snapshotLocService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PicService picService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    //工人提交所有图片和机器二维码时触发,创建snapshot表单
    public ResultData create(SnapshotForm form, HttpServletRequest request) {
        ResultData result = new ResultData();

        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String picPath = form.getPicPath().trim();
        String locationLng = form.getLongitude().trim();
        String locationLat = form.getLatitude().trim();

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
        final Map<String, Object> condition1 = condition;
        new Thread(() -> {
            final ResultData response1 = assignService.fetchAssign(condition1);
            if (response1.getResponseCode() == ResponseCode.RESPONSE_OK) {
                Assign assign = ((List<Assign>) response1.getData()).get(0);
                assign.setAssignStatus(AssignStatus.FINISHED);
                assignService.updateAssign(assign);
            }
        }).start();

        //new a thread to save information
        String snapshotId = ((Snapshot) response.getData()).getSnapshotId();
        String ip = IPUtil.getIP(request);
        new Thread(() -> {
            fillSnapshotId(picPath,snapshotId);                                 //修改pic表
            saveSnapshotLocation(snapshotId, locationLng, locationLat, ip);     //修改location表
            fileMapService.createPicMap(picPath);                               //修改filemap表
            tempFileMapService.deleteValidPicMapFromTempFileMap(picPath);       //修改tempfilemap表
        }).start();

        return result;
    }

    //根据经纬度解析地址并保存
    private void saveSnapshotLocation(String snapshotId, String locationLng, String locationLat, String ip) {
        double lng = 0.0, lat = 0.0;
        String locationPlace = "";
        //传过来的经纬度都不为空
        if (!StringUtils.isEmpty(locationLng) && !StringUtils.isEmpty(locationLat)) {
            try {
                lng = Double.parseDouble(locationLng);
                lat = Double.parseDouble(locationLat);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("输入了不可转换为double的字符串");
            }
            ResultData response = locationService.ll2description(locationLng, locationLat);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                LinkedHashMap<String, Object> locInformation = (LinkedHashMap<String, Object>) response.getData();
                locationPlace = (String) locInformation.get("address");
            }
        } else {
            ResultData response = locationService.ip2address(ip);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                LinkedHashMap<String, Object> locInformation = (LinkedHashMap<String, Object>) response.getData();
                LinkedHashMap<String, Object> ad_info = (LinkedHashMap<String, Object>) locInformation.get("ad_info");
                locationPlace = new StringBuilder((String) ad_info.get("nation"))
                        .append((String) ad_info.get("province"))
                        .append((String) ad_info.get("city"))
                        .toString();
            }
        }
        SnapshotLoc snapshotLoc = new SnapshotLoc(snapshotId, lng, lat, locationPlace);
        snapshotLocService.createSnapshotLoc(snapshotLoc);
    }

    //根据上传的picUrl来更新pic表中的snapshot_id那一列
    private ResultData fillSnapshotId(String picUrl,String snapshotId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        String[] urls = picUrl.split(",");
        for (String url : urls) {
            condition.clear();
            condition.put("pic_address", url);
            ResultData response = picService.fetchPic(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            Pic pic = new Pic();
            pic.setPicId(((List<Pic>)response.getData()).get(0).getPicId());
            pic.setMemberPhone(snapshotId);
            picService.updatePic(pic);
            result.setDescription("success to fill the snapshotId to pic");
        }
        return result;
    }
}
