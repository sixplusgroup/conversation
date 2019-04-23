package finley.gmair.controller;

import finley.gmair.form.installation.TeamForm;
import finley.gmair.model.installation.Team;
import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TeamController
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/28 1:03 PM
 */
@RestController
@RequestMapping("/install/team")
public class TeamController {
    private Logger logger = LoggerFactory.getLogger(TeamController.class);

    @Autowired
    private TeamService teamService;

    @PostMapping("/create")
    public ResultData create(TeamForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getTeamName()) || StringUtils.isEmpty(form.getTeamArea())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保团队的信息录入完整");
            return result;
        }
        //根据表单内容构建安装团队
        String name = form.getTeamName().trim();
        String area = form.getTeamArea().trim();
        String description = form.getTeamDescription();
        Team team = new Team(name, area, description);
        //存入数据库
        ResultData response = teamService.create(team);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建安装团队失败，请稍后尝试");
        }
        return result;
    }

    @GetMapping("/list")
    public ResultData list(Integer start, Integer length) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response;
        if (start == null || length == null) {
            response = teamService.fetch(condition);
        } else {
            response = teamService.fetch(condition, start, length);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有符合条件的安装团队");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装团队失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping("/{teamId}/info")
    public ResultData detail(@PathVariable("teamId") String teamId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有符合条件的安装团队");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装团队失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(((List) response.getData()).get(0));
        }
        return result;
    }
}
