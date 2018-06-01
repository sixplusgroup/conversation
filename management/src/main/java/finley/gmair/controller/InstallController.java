package finley.gmair.controller;

import finley.gmair.form.installation.*;
import finley.gmair.service.InstallService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/reconnaissance/{reconnaissanceId}/process")
    public ResultData reconnaissanceProcess(@PathVariable String reconnaissanceId, ReconnaissanceForm form) {
        return installService.reconnaissanceProcess(reconnaissanceId, form.getOrderId(), form.getSetupMethod(),
                form.getDescription(), form.getReconDate(), form.getReconStatus());
    }

    @PostMapping(value = "/assign/allocate")
    public ResultData allocateInstallationAssign(AllocateForm allocateForm) {
        return installService.allocateInstallationAssign(allocateForm.getAssignId(),
                allocateForm.getTeamName(), allocateForm.getMemberName(), allocateForm.getInstallDate());
    }

    @GetMapping(value = "/assign/finishedinfo")
    public ResultData getFinishedInfo(String assignId) {
        return installService.finishedInfo(assignId);
    }
}
