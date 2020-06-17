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
     * 安装负责人关注一个团队
     *
     * @param memberId
     * @param teamId
     * @return
     */
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

    /**
     * 安装负责人查看自己负责的团队列表
     *
     * @param memberId
     * @return
     */
    @GetMapping("/watch/teamList")
    public ResultData watches(String memberId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供成员的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberId", memberId);
        condition.put("blockFlag",false);
        ResultData response = memberService.fetchTeams(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取相关团队信息失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到相关的团队信息");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 通过memberId和teamId伪删除关注团队
     * @param memberId
     * @param teamId
     * @return
     */
    @PostMapping("/block")
    public ResultData block(String memberId, String teamId){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)||StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确认memberId和teamId均已提供");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberId", memberId);
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);
        ResultData response = memberService.blockTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("关注团队删除成功");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("关注团队删除失败，请稍后尝试");
        }
        return result;
    }

    /**
     * 根据团队id查看所有相关负责人
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
