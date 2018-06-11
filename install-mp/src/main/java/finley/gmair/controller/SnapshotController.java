package finley.gmair.controller;


import finley.gmair.form.installation.SnapshotForm;
import finley.gmair.form.message.MessageForm;
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

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = RequestMethod.POST, value = "/create")
    //工人提交所有图片和机器二维码时触发,创建snapshot表单
    public ResultData create(SnapshotForm form, HttpServletRequest request) {
        ResultData result = new ResultData();

        //check whether input is empty
        if (StringUtils.isEmpty(form.getQrcode()) || StringUtils.isEmpty(form.getWechatId()) || StringUtils.isEmpty(form.getPicPath()) || StringUtils.isEmpty(form.getInstallType())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        String qrcode = form.getQrcode().trim();
        String wechatId = form.getWechatId().trim();
        String picPath = form.getPicPath().trim();
        String locationLng = form.getLongitude().trim();
        String locationLat = form.getLatitude().trim();
        boolean net = form.isNet();
        String installType = form.getInstallType().trim();



        //according to the qrcode,find the assignId.
        String assignId = "";
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
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
        Snapshot snapshot = new Snapshot(assignId, qrcode, wechatId, memberPhone, picPath, net, installType);
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

        //new a thread to save information
        final String assignID = assignId;
        final String snapshotId = ((Snapshot) response.getData()).getSnapshotId();
        final String ip = IPUtil.getIP(request);
        new Thread(() -> {
            try {
                updateToFinished(assignID);                                         //修改assign表
                fillSnapshotId(picPath, snapshotId);                                //修改pic表
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                saveSnapshotLocation(snapshotId, locationLng, locationLat, ip);     //修改location表
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                fileMapService.createPicMap(picPath);                               //修改filemap表

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                //TODO
                //该方法如果失败了,会导致没有把有用的记录从Tempfile表中删去,这会导致冗余数据.
                tempFileMapService.deleteValidPicMapFromTempFileMap(picPath);       //修改tempfilemap表
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                sendMessage(snapshotId);                                            //发送短信
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();

        return result;
    }

    //把Assign表单状态更新为FINISHED
    private void updateToFinished(String assignId) {
        Assign assign = new Assign();
        assign.setAssignId(assignId);
        assign.setAssignStatus(AssignStatus.FINISHED);
        assignService.updateAssign(assign);
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
                return;
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

    //根据上传的picUrl来填写对应的snapshot_id,并更新occupied状态位
    private ResultData fillSnapshotId(String picUrl, String snapshotId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        String[] urls = picUrl.split(",");
        for (String url : urls) {
            condition.clear();
            condition.put("picAddress", url);
            ResultData response = picService.fetchPic(condition);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK)
                continue;
            Pic pic = ((List<Pic>) response.getData()).get(0);
            pic.setSnapshotId(snapshotId);
            pic.setOccupied(true);
            picService.updatePic(pic);
            result.setDescription("success to update pic");
        }
        return result;
    }

    //给用户发短信
    @RequestMapping(method = RequestMethod.POST, value = "/send")
    private void sendMessage(String snapshotId){
        Map<String,Object> condtion = new HashMap<>();
        condtion.put("snapshotId",snapshotId);
        condtion.put("blockFlag",false);
        ResultData response = snapshotService.fetchSnapshot(condtion);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK)
            return;

        String assignId = ((List<Snapshot>)response.getData()).get(0).getAssignId();

        condtion.clear();
        condtion.put("assignId",assignId);
        condtion.put("blockFlag",false);
        response = assignService.fetchAssign(condtion);
        if(response.getResponseCode() != ResponseCode.RESPONSE_OK)
            return;
        String phone = ((List<Assign>)response.getData()).get(0).getConsumerPhone();
        String text = "尊敬的用户,你们家的新风设备已经安装完毕,请对本次安装过程做一个反馈:1.满意 2.一般 3.不满意";
        messageService.sendOne(phone,text);
    }
}
