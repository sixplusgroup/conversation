package finley.gmair.controller;

import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
        ResultData response = memberService.bindWechat(openid, phone);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("绑定失败，请稍后尝试");
        }
        return result;
    }


//    @RequestMapping(method = RequestMethod.GET, value = "/findphone")
//    public ResultData findPhone(String memberPhone) {
//        ResultData result = new ResultData();
//        Map<String, Object> condition = new HashMap<>();
//        condition.put("memberPhone", memberPhone);
//        condition.put("blockFlag", false);
//        ResultData response = memberService.fetchMember(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription("Fail to get member info");
//        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            result.setResponseCode(ResponseCode.RESPONSE_NULL);
//            result.setDescription("No member info at the moment");
//        } else {
//            result.setResponseCode(ResponseCode.RESPONSE_OK);
//            result.setData(response.getData());
//            result.setDescription("Success to get member info");
//        }
//        return result;
//    }
//
//    @RequestMapping(method = RequestMethod.GET, value = "/findwechat")
//    public ResultData findwechat(String wechatId) {
//        ResultData result = new ResultData();
//        Map<String, Object> condition = new HashMap<>();
//        condition.put("wechatId", wechatId);
//        condition.put("blockFlag", false);
//        ResultData response = memberService.fetchMember(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription("Fail to get member info.");
//        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            result.setResponseCode(ResponseCode.RESPONSE_NULL);
//            result.setDescription("No member info at the moment.");
//        } else {
//            result.setResponseCode(ResponseCode.RESPONSE_OK);
//            result.setData(response.getData());
//            result.setDescription("Success to get member info.");
//        }
//        return result;
//    }
//
//    //根据微信号查询该工人是否有权限解绑机器二维码和用户的绑定
//    @RequestMapping(method = RequestMethod.GET, value = "/checkleader")
//    public ResultData checkLeader(String wechatId) {
//        ResultData result = new ResultData();
//        Map<String, Object> condition = new HashMap<>();
//        condition.put("wechatId", wechatId);
//        condition.put("memberRole", MemberRole.LEADER.getValue());
//        condition.put("blockFlag", false);
//        ResultData response = memberService.fetchMember(condition);
//        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription("server is busy now");
//        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
//            result.setResponseCode(ResponseCode.RESPONSE_NULL);
//            result.setData(0);
//            result.setDescription("he is a ordinary worker");
//        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
//            result.setResponseCode(ResponseCode.RESPONSE_OK);
//            result.setData(1);
//            result.setDescription("he is a leader worker");
//        }
//        return result;
//    }
}
