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
import java.util.Map;

@RestController
@RequestMapping("/installation/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResultData createMember(MemberForm form) {
        ResultData result = new ResultData();

        if (StringUtils.isEmpty(form.getTeamId())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the team ID");
            return result;
        }
        if (StringUtils.isEmpty(form.getMemberName())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the member name");
            return result;
        }
        if (StringUtils.isEmpty(form.getMemberPhone())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            ;
            result.setDescription("Please privide the member phone");
            return result;
        }

        //check whether the team exist
        String teamId = form.getTeamId().trim();
        String memberPhone = form.getMemberPhone().trim();
        String memberName = form.getMemberName().trim();

        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("member with phone: ").append(memberPhone).append(" already exist").toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("Team: ").append(teamId).append(" is not exist").toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("fetch Team: ").append(teamId).append(" error").toString());
            return result;
        }

        //create the the member
        Member member = new Member(teamId, memberPhone, memberName);
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

    @RequestMapping(method = RequestMethod.GET, value = "/{memberPhone}/info")
    public ResultData list(@PathVariable("memberPhone") String memberPhone) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get member info");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No member info at the moment");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to get member info");
        }
        return result;
    }

    @PostMapping("/update")
    public ResultData update(MemberForm form) {
        ResultData result = new ResultData();

        return result;
    }

    @PostMapping("/exist")
    public ResultData exist(String openid) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechat_id", openid);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No user with openid: ").append(openid).append(" found, need to bind first").toString());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to query the information, please try again later");
        }
        return result;
    }
}
