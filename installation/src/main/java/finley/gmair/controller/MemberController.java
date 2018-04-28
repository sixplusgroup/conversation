package finley.gmair.controller;

import finley.gmair.form.installation.MemberForm;
import finley.gmair.model.installation.Member;
import finley.gmair.service.MemberService;
import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installation/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    @RequestMapping(method = RequestMethod.POST, value="/create")
    public ResultData createMember(MemberForm form)
    {
        ResultData result = new ResultData();
        String teamId = form.getTeamId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String memberName = form.getMemberName().trim();

        //check whether input is empty
        if(StringUtils.isEmpty(teamId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the team ID");
            return result;
        }
        if(StringUtils.isEmpty(memberName)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the member name");
            return result;
        }
        if(StringUtils.isEmpty(memberPhone))
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);;
            result.setDescription("Please privide the member phone");
            return result;
        }

        //check whether the team exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("team is not exist");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        //check weather the phone has been registered
        condition.clear();
        condition.put("memberPhone",memberPhone);
        condition.put("blockFlag",false);
        response = memberService.fetchMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("member phone has been registered");
            return result;
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,try again later");
            return result;
        }

        //create the the member
        Member member = new Member(teamId,memberPhone,memberName);
        response = memberService.createMember(member);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the member");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(response.getData());
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value="/setwechat")
    public ResultData setwechat(String memberPhone,String wechatId) {
        ResultData result = new ResultData();

        memberPhone=memberPhone.trim();
        wechatId=wechatId.trim();

        //check empty input
        if(StringUtils.isEmpty(memberPhone)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the assignId");
            return result;
        }
        if(StringUtils.isEmpty(wechatId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the installDate");
            return result;
        }

        //according to the memberPhone,find the member
        Member member = new Member();
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone",memberPhone);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            member = ((List<Member>)response.getData()).get(0);
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_NULL){
            result.setDescription("can not find the member with memberPhone");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        }

        //update the member
        member.setWechatId(wechatId);
        response = memberService.updateMember(member);
        if(response.getResponseCode() == ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(member);
            result.setDescription("success to update the member");
        }
        else if(response.getResponseCode() == ResponseCode.RESPONSE_ERROR){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        }

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findphone")
    public ResultData findPhone(String memberPhone)
    {
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("memberPhone",memberPhone);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get member info");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No member info at the moment");
        }
        else
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to get member info");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/findwechat")
    public ResultData findwechat(String wechatId){
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("wechatId",wechatId);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchMember(condition);
        if(response.getResponseCode()==ResponseCode.RESPONSE_ERROR)
        {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get member info.");
        }
        else if(response.getResponseCode()==ResponseCode.RESPONSE_NULL)
        {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No member info at the moment.");
        }
        else
        {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to get member info.");
        }
        return result;
    }




}
