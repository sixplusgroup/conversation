package finley.gmair.controller;

import finley.gmair.form.installation.MemberForm;
import finley.gmair.model.installation.Member;
import finley.gmair.model.installation.MemberRole;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MemberController
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/22 4:02 PM
 */
@RestController
@RequestMapping("/install/member")
public class MemberController {
    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    @PostMapping("/create")
    public ResultData create(MemberForm form) {
        ResultData result = new ResultData();
        if (org.springframework.util.StringUtils.isEmpty(form.getTeamId()) || org.springframework.util.StringUtils.isEmpty(form.getMemberName()) || org.springframework.util.StringUtils.isEmpty(form.getMemberPhone()) || org.springframework.util.StringUtils.isEmpty(MemberRole.fromValue(form.getMemberRole()))) {
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
        condition.put("memberPhone", memberPhone);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("手机号: " + form.getMemberPhone() + "的用户已存在");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建用户失败，请稍后尝试");
            return result;
        }
        Member member = new Member(teamId, memberPhone, memberName, memberRole);
        response = memberService.create(member);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            response.setDescription("创建用户成功");
        } else {
            response.setResponseCode(ResponseCode.RESPONSE_ERROR);
            response.setDescription("创建用户失败，请稍后尝试");
        }
        return result;
    }

    @GetMapping("/list")
    public ResultData list(String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装团队的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        ResultData response = memberService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取团队成员信息失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到相关的团队成员");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    @GetMapping("/profile")
    public ResultData profile(String openid, String phone, String memberId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(openid) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供openid或电话号码或成员信息中的至少一个");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(openid)) {
            condition.put("wechatId", openid);
        }
        if (!StringUtils.isEmpty(phone)) {
            condition.put("memberPhone", phone);
        }
        if (!StringUtils.isEmpty(memberId)) {
            condition.put("memberId", memberId);
        }
        condition.put("blockFlag", false);
        ResultData response = memberService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到相关用户的信息");
            return result;
        }
        result.setData(((List) response.getData()).get(0));
        return result;
    }

    @PostMapping("/bind")
    public ResultData bindWechat(String openid, String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供用户的openid和电话号码");
            return result;
        }
        //根据电话号码查询到用户的信息
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberPhone", phone);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("当前不存在电话号码为: " + phone + "的用户");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询用户信息异常, 请稍后尝试.");
        }
        Member member = ((List<Member>) response.getData()).get(0);
        //更新用户的信息
        condition.clear();
        condition.put("memberId", member.getMemberId());
        condition.put("wechatId", openid);
        response = memberService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("设置用户: " + member.getMemberId() + "的openid为: " + openid + "失败，请稍后尝试");
            return result;
        }
        result.setDescription("openid: " + openid + "与手机号: " + phone + "绑定成功");
        return result;
    }

    @PostMapping("/watch/team")
    public ResultData watch(String memberId, String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供成员以及所需要负责的团队的信息");
            return result;
        }
        ResultData response = memberService.watchTeam(memberId, teamId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("安装负责人" + memberId + "成功负责" + teamId + "对应的团队");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("安装负责人与团队绑定失败，请稍后尝试");
        }
        return result;
    }

}
