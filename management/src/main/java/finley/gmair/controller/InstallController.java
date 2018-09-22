package finley.gmair.controller;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.installation.*;
import finley.gmair.service.InstallService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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


    @GetMapping("/assign/list")
    public ResultData assignList() {
        ResultData result = new ResultData();
        JSONObject data = new JSONObject();
        ResultData response = installService.todoAssignList();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            data.put("todo", ((List) response.getData()).size());
        } else {
            data.put("todo", 0);
        }
        response = installService.assignedList();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            data.put("assigned", ((List) response.getData()).size());
        } else {
            data.put("assigned", 0);
        }
        response = installService.processingAssignList();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            data.put("processing", ((List) response.getData()).size());
        } else {
            data.put("processing", 0);
        }
        response = installService.finishedList();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            data.put("finished", ((List) response.getData()).size());
        } else {
            data.put("finished", 0);
        }
        response = installService.closedList();
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            data.put("closed", ((List) response.getData()).size());
        } else {
            data.put("closed", 0);
        }
        result.setData(data);
        return result;
    }

    @GetMapping("/assign/todo")
    public ResultData todoList() {
        return installService.todoAssignList();
    }

    @GetMapping("/assign/assigned")
    public ResultData assignedList() {
        return installService.assignedList();
    }

    @GetMapping("/assign/processing")
    public ResultData processingList() {
        return installService.processingAssignList();
    }

    @GetMapping("/assign/finished")
    public ResultData finishedList() {
        return installService.finishedList();
    }

    @GetMapping("/assign/closed")
    public ResultData closedList() {
        return installService.closedList();
    }

    @GetMapping("/assign/detail/list")
    public ResultData assignDetailList(int status) {
        return installService.detailList(status);
    }

    @GetMapping("/assign/feedback/info")
    public ResultData assignFeedback(String assignId) {
        return installService.assignFeedback(assignId);
    }

    @GetMapping("/reconnaissance/order/{orderId}")
    public ResultData recoList(@PathVariable("orderId") String orderId) {
        return installService.orderReconnaissanceList(orderId);
    }
}
