package finley.gmair.controller;

import finley.gmair.form.installation.AllocateForm;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.Member;
import finley.gmair.model.installation.Team;
import finley.gmair.service.AssignService;
import finley.gmair.service.MemberService;
import finley.gmair.service.TeamService;
import finley.gmair.util.DateFormatUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/installation/assign")
public class AssignController {
    @Autowired
    private AssignService assignService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MemberService memberService;


    /**
     * This is the method to create an install assignment for a machine
     * machine is represented by the qrcode
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(AssignForm form) {
        ResultData result = new ResultData();

        //check whether input is empty
        if (StringUtils.isEmpty(form.getQrcode()) || StringUtils.isEmpty(form.getConsumerConsignee())
                || StringUtils.isEmpty(form.getConsumerPhone()) || StringUtils.isEmpty(form.getConsumerAddress())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }
        String qrcode = form.getQrcode().trim();
        String consumerConsignee = form.getConsumerConsignee().trim();
        String consumerPhone = form.getConsumerPhone().trim();
        String consumerAddress = form.getConsumerAddress().trim();

        //create the the assign and save
        Assign assign = new Assign(qrcode, consumerConsignee, consumerPhone, consumerAddress);
        ResultData response = assignService.createAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription(new StringBuffer("Install assign for ").append(form.getQrcode()).append(" has been created.").toString());
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to create install assign for ").append(form.getQrcode()).toString());
            return result;
        }
    }

    /**
     * This method is used to assign tasks to target install team & member
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/allocate")
    public ResultData allocate(AllocateForm form) {
        ResultData result = new ResultData();

        //check empty input
        if (StringUtils.isEmpty(form.getAssignId()) || StringUtils.isEmpty(form.getTeamName())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }

        String assignId = form.getAssignId().trim();
        String teamName = form.getTeamName().trim();

        String memberName = StringUtils.isEmpty(form.getMemberName()) ? "" : form.getMemberName().trim();
        String installTime = StringUtils.isEmpty(form.getInstallDate()) ? "" : form.getInstallDate().trim();

        //according to the teamName,find the teamId.
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamName", teamName);
        condition.put("blockFlag", false);
        String teamId = "";
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            teamId = ((List<Team>) response.getData()).get(0).getTeamId();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No team with name: ").append(teamName).append(" found").toString());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find install team");
            return result;
        }

        condition.clear();

        //according to the memberName,find the memberId.
        String memberId = "";
        condition.put("teamId", teamId);
        condition.put("memberName", memberName);
        condition.put("blockFlag", false);
        response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            memberId = ((List<Member>) response.getData()).get(0).getMemberId();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find team member");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No member with name: ").append(memberName).append(" found").toString());
            return result;
        }

        condition.clear();

        //according to the assignId,find the assign record.
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find the assign");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        Assign assign = ((List<Assign>) response.getData()).get(0);

        //update the assign
        assign.setTeamId(teamId);
        assign.setMemberId(memberId);
        if (StringUtils.isEmpty(installTime) || DateFormatUtil.convertToLocalDate(installTime) == null)
            assign.setAssignStatus(AssignStatus.ASSIGNED);
        else {
            assign.setAssignStatus(AssignStatus.PROCESSING);
            LocalDateTime localDateTime = LocalDateTime.of(DateFormatUtil.convertToLocalDate(installTime), LocalTime.MIN);
            assign.setAssignDate(Timestamp.valueOf(localDateTime));
        }

        response = assignService.updateAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("Succeed to update the install assign information.");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update current install assign");
        }
        return result;
    }

    /**
     * This method is used to retrieve corresponding assign information
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list(String status) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(status)) {
            condition.put("assignStatus", status);
        }
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No install assignment found").toString());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to find any install assign").toString());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assign list");
        }
        return result;
    }

    /**
     * This method is used to retrieve all todo assign information
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/todolist")
    public ResultData todolist() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.TODOASSIGN.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no todo assign found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the todoassign list ");
        }
        return result;
    }

    //管理员查看已分配安装任务时触发,拉取已分配状态的安装任务列表
    @RequestMapping(method = RequestMethod.GET, value = "/assignedlist")
    public ResultData assignedlist() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.ASSIGNED.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assigned assign found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later!");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assigned assign list ");
        }
        return result;
    }

    //管理员查看正在安装任务时触发,拉取正在安装状态的安装任务列表
    @RequestMapping(method = RequestMethod.GET, value = "/processinglist")
    public ResultData processinglist() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no processing assign found ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the processing assign list");
        }
        return result;
    }

    //管理员查看已完成安装任务时触发,拉取已完成状态的安装任务列表
    @RequestMapping(method = RequestMethod.GET, value = "/finishedlist")
    public ResultData finishedlist() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no finished assign found ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later  ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the finished assign list");
        }
        return result;
    }

    @GetMapping("/closedlist")
    public ResultData closedList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.CLOSED.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No closed assign found.");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to get closed assign.");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping("/detail/list")
    public ResultData listWithDetailName(String qrcode, Integer status) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        if (!StringUtils.isEmpty(qrcode)) {
            condition.put("codeValue", qrcode);
        }
        if (!StringUtils.isEmpty(status)) {
            condition.put("assignStatus", status);
        }
        ResultData response = assignService.fetchAssignWithDetailName(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later  ");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assign list by qrcode");
        }
        return result;
    }

    //查询Finished状态的安装单信息
    @RequestMapping(method = RequestMethod.GET, value = "/finishedinfo")
    public ResultData finishedinfo(String assignId) {
        ResultData result = new ResultData();
        //check empty
        if (StringUtils.isEmpty(assignId)) {
            result.setDescription("please provide assignId");
            return result;
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign3(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found!");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now, please try again later!");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assign list by assignId");
        }
        return result;
    }

    //计算各个状态安装单的数量
    @RequestMapping(method = RequestMethod.GET, value = "/count")
    public ResultData countAssign() {
        ResultData result = new ResultData();
        List<Integer> list = new ArrayList<>();
        int todoassign = ((List<Assign>) (todolist().getData())).size();
        int assigned = ((List<Assign>) (assignedlist().getData())).size();
        int processing = ((List<Assign>) (processinglist().getData())).size();
        int finished = ((List<Assign>) (finishedlist().getData())).size();
        list.add(todoassign);
        list.add(assigned);
        list.add(processing);
        list.add(finished);
        result.setData(list);
        return result;
    }

    @PostMapping("/postpone")
    public ResultData postpone(String assignId, String date) {
        ResultData result = new ResultData();

        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(date)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }

        //fetch the assign
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to fetch assign: ").append(assignId).toString());
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No assign found with assign id: ").append(assignId).toString());
            return result;
        }
        Assign assign = ((List<Assign>) response.getData()).get(0);
        LocalDateTime localDateTime = LocalDateTime.of(DateFormatUtil.convertToLocalDate(date), LocalTime.MIN);
        assign.setAssignDate(Timestamp.valueOf(localDateTime));
        response = assignService.updateAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(assign);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to update assign");
        }
        return result;
    }

    @PostMapping("/cancel")
    public ResultData cancel(String assignId, String description) {
        ResultData result = new ResultData();
        //check the input
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(description)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided.");
            return result;
        }

        //fetch assign using assignId
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        condition.put("blockFlag", false);

        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find install assign.");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No install assign with id: ").append(assignId).toString());
            return result;
        }
        Assign assign = ((List<Assign>) response.getData()).get(0);
        assign.setAssignStatus(AssignStatus.CLOSED);

        response = assignService.updateAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to cancel assign: ").append(assignId).toString());
            return result;
        }
        result.setData(assign);
        return result;
    }

}
