package finley.gmair.controller;

import finley.gmair.form.installation.MemberForm;
import finley.gmair.model.installation.MemberRole;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        condition.put("blockFlag", false);

        return result;
    }
}
