package finley.gmair.controller;

import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/install-mp/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    //工人第一次登录时触发,将微信和手机号绑定

    /**
     * if any member with the given phone, set his/her openid
     *
     * @param
     * @return
     */
    @PostMapping("/bind")
    public ResultData bind(String phone, String openid) {
        ResultData result = new ResultData();
        //check empty input
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(openid)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供电话号码和用户的openid");
            return result;
        }
        result = memberService.bindWechat(openid, phone);
        return result;
    }

    /**
     * 安装负责人查看安装团队的成员
     *
     * @param teamId
     * @return
     */
    @GetMapping("/list")
    public ResultData list(String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装团队的信息");
            return result;
        }
        result = memberService.list(teamId);
        return result;
    }
}
