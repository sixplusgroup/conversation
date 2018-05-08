package finley.gmair.controller;

import finley.gmair.model.installation.Member;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/install-mp/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //工人第一次登录时触发,将微信和手机号绑定
    @RequestMapping(method = RequestMethod.POST, value = "/setwechat")
    public ResultData setwechat(String memberPhone, String wechatId) {
        ResultData result = new ResultData();

        memberPhone = memberPhone.trim();
        wechatId = wechatId.trim();

        //check empty input
        if (StringUtils.isEmpty(memberPhone) || StringUtils.isEmpty(wechatId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide the all information");
            return result;
        }

        //according to the memberPhone,find the member
        Member member = new Member();
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            member = ((List<Member>) response.getData()).get(0);
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setDescription("can not find the member with memberPhone");
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        //update the member
        member.setWechatId(wechatId);
        response = memberService.updateMember(member);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(member);
            result.setDescription("success to update the member");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        }

        return result;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/findphone")
    public ResultData findPhone(String memberPhone) {
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

    @RequestMapping(method = RequestMethod.GET, value = "/findwechat")
    public ResultData findwechat(String wechatId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId", wechatId);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get member info.");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No member info at the moment.");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to get member info.");
        }
        return result;
    }
}
