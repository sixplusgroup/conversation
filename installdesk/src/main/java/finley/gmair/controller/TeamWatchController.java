package finley.gmair.controller;

import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TeamWatchController
 * @Description: TODO
 * @Author zhang
 * @Date 2019/11/4 4:00 PM
 */
@RestController
@RequestMapping("/install/teamwatch")
public class TeamWatchController {

    @Autowired
    private MemberService memberService;

    /**
     * get the leaderlist by the team
     * @param teamId
     * @return
     */
    @GetMapping("/list")
    public ResultData list(String teamId){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装团队的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        //condition.put("blockFlag",false);
        ResultData response = memberService.fetchMemberTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取相关负责人信息失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到相关的负责人信息");
            return result;
        }

        result.setData(response.getData());
        return result;
    }
}
