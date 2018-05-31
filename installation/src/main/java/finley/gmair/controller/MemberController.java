package finley.gmair.controller;

import finley.gmair.form.installation.MemberForm;
import finley.gmair.model.installation.Member;
import finley.gmair.model.installation.MemberRole;
import finley.gmair.service.MemberService;
import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/installation/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    //管理员在后台选择创建一个工人时触发
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData createMember(MemberForm form) {
        ResultData result = new ResultData();

        if (StringUtils.isEmpty(form.getTeamId()) || StringUtils.isEmpty(form.getMemberName()) || StringUtils.isEmpty(form.getMemberPhone()) || StringUtils.isEmpty(MemberRole.fromValue(form.getMemberRole()))) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all the required information is provided.");
            return result;
        }

        String teamId = form.getTeamId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String memberName = form.getMemberName().trim();
        MemberRole memberRole = MemberRole.fromValue(form.getMemberRole());

        //check whether the team exist
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure the team you select is valid.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find the team information, please try again later.");
            return result;
        }

        //check weather the phone has been registered
        condition.clear();
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("The member phone has already been registered by another member.");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find the phone information, please try again later.");
            return result;
        }

        //create the the member
        Member member = new Member(teamId, memberPhone, memberName, memberRole);
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

    //
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData members(String teamId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(teamId)) {
            condition.put("teamId", teamId);
        }
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no member found ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later  ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the member list");
        }
        return result;
    }

}
