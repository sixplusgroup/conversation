package finley.gmair.controller;

import finley.gmair.form.installation.InstallDateForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.Member;
import finley.gmair.service.AssignService;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/install-mp/assign")
public class AssignController {
    @Autowired
    private AssignService assignService;

    @Autowired
    private MemberService memberService;

    //工人确认带货安装时触发,创建安装任务表单
    @RequestMapping(method = RequestMethod.POST, value= "/withmachine")
    public ResultData withmachine(String wechatId,String qrcode){
        ResultData result = new ResultData();

        wechatId = wechatId.trim();
        qrcode = qrcode.trim();

        //check whether input is empty
        if(StringUtils.isEmpty(wechatId) || StringUtils.isEmpty(qrcode)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        //according to wechatId,find the member.
        Member member = new Member();
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId",wechatId);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            member = ((List<Member>)response.getData()).get(0);
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the member with wechatId");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        }

        //create the assign and save
        Assign assign = new Assign();
        assign.setQrcode(qrcode);
        assign.setTeamId(member.getTeamId());
        assign.setMemberId(member.getMemberId());
        assign.setAssignStatus(AssignStatus.PROCESSING);
        assign.setAssignDate(new Timestamp(System.currentTimeMillis()));
        assign.setConsumerConsignee("");
        assign.setConsumerPhone("");
        assign.setConsumerAddress("");
        response = assignService.createAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to create the assign.");
            return result;
        }
        else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the assign.");
            return result;
        }
    }

    //工人选择安装时间并提交时触发,修改任务状态,安装日期
    @RequestMapping(method = RequestMethod.POST , value="/date")
    public ResultData date(InstallDateForm form){

        ResultData result = new ResultData();

        String assignId = form.getAssignId().trim();
        String installDate = form.getInstallDate().trim();

        //check empty input
        if(StringUtils.isEmpty(assignId) || StringUtils.isEmpty(installDate)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }


        //according to the assignId,find the assign record.
        Assign assign = new Assign();
        Map<String,Object> condition = new HashMap<>();
        condition.put("assignId",assignId);
        condition.put("blockFlag",false);
        ResultData response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the assign");
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later!");
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            assign = ((List<Assign>)response.getData()).get(0);
        }

        //update the assign
        if(assign.getAssignStatus().getValue() < AssignStatus.PROCESSING.getValue()) {
            assign.setAssignDate(Timestamp.valueOf(installDate));
            assign.setAssignStatus(AssignStatus.PROCESSING);
            response = assignService.updateAssign(assign);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(response.getData());
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to update date");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to update date");
            }
        }
        else{
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can not update the processing or finished assign record");
        }
        return result;
    }

    //工人选择反馈界面时触发,拉取可反馈安装任务列表.
    @RequestMapping(method = RequestMethod.GET, value = "/feedbacklist")
    public ResultData feedbacklist(String wechatId){
        ResultData result = new ResultData();

        wechatId = wechatId.trim();

        //check empty input
        if(StringUtils.isEmpty(wechatId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the memberId");
            return result;
        }

        //according to wechatId, find memberId
        String memberId = "";
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId",wechatId);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            memberId = ((List<Member>)response.getData()).get(0).getMemberId();
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not found the member");
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }

        //fetch the PROCESSING assign list.
        condition.clear();
        condition.put("assignStatus",AssignStatus.PROCESSING.getValue());
        condition.put("memberId",memberId);
        condition.put("blockFlag",false);
        response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later!  ");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assigned list");
        }
        return result;
    }

    //工人查看分配给自己的安装任务时触发.
    @RequestMapping(method = RequestMethod.GET, value = "/workertodo")
    public ResultData workertodo(String wechatId){
        ResultData result = new ResultData();

        wechatId = wechatId.trim();

        //check empty input
        if(StringUtils.isEmpty(wechatId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the memberId");
            return result;
        }

        //according to wechatId, find memberId
        String memberId = "";
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId",wechatId);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            memberId = ((List<Member>)response.getData()).get(0).getMemberId();
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not found the member!");
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }

        //fetch the assigned assign list.
        condition.clear();
        condition.put("assignStatus",AssignStatus.ASSIGNED.getValue());
        condition.put("memberId",memberId);
        condition.put("blockFlag",false);
        response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later  ");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assigned list");
        }
        return result;
    }

    //工人查看分配给自己的团队但没有指定具体人的安装任务时触发
    @RequestMapping(method = RequestMethod.GET, value = "/teamtodo")
    public ResultData teamtodo(String teamId){
        ResultData result = new ResultData();

        teamId = teamId.trim();

        //check empty input
        if(StringUtils.isEmpty(teamId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the teamId");
            return result;
        }

        //fetch the assign list
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus",AssignStatus.ASSIGNED.getValue());
        condition.put("memberId","");
        condition.put("teamId",teamId);
        condition.put("blockFlag",false);

        ResultData response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found ");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assigned list");
        }

        return result;
    }

    //工人扫二维码时触发,查找assign表中是否存在qrcode为某值的记录
    @RequestMapping(method = RequestMethod.GET, value = "/qrcode")
    public ResultData qrcode(String qrcode)
    {
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("qrcode",qrcode);
        condition.put("blockFlag",false);
        ResultData response = assignService.fetchAssign(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to find the assign with qrcode");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no such assign found");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the assign with qrcode.");
        }
        return result;
    }

}
