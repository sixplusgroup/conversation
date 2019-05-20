package finley.gmair.controller;

import finley.gmair.service.TeamService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TeamController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/10 10:52 AM
 */
@RestController
@RequestMapping("/install-mp/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/profile")
    public ResultData detail(String teamId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装团队的信息");
            return result;
        }
        result = teamService.profile(teamId);
        return result;
    }
}
