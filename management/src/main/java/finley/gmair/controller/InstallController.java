package finley.gmair.controller;

import finley.gmair.form.installation.MemberForm;
import finley.gmair.form.installation.TeamForm;
import finley.gmair.service.InstallService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/management/install")
public class InstallController {

    @Autowired
    private InstallService installService;

    @GetMapping("/team/list")
    public ResultData teamList() {
        return installService.fetchTeamList();
    }

    @PostMapping("/team/create")
    public ResultData teamCreate(TeamForm form) {
        return installService.createTeam(form.getTeamName(), form.getTeamArea(), form.getTeamDescription());
    }

    @GetMapping("/member/list")
    public ResultData memberList(String teamId) {
        return installService.fetchMemberList(teamId);
    }

    @PostMapping("/member/create")
    public ResultData memberCreate(MemberForm form) {
        return installService.createMember(form.getTeamId(), form.getMemberPhone(), form.getMemberName(), form.getMemberRole());
    }
}
