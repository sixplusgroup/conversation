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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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


    //订单发货时由order模块调用触发,创建安装任务表单
    @RequestMapping(method = RequestMethod.POST, value = "/create")
    public ResultData create(AssignForm form) {
        ResultData result = new ResultData();

        String qrcode = form.getQrcode().trim();
        String consumerConsignee = form.getConsumerConsignee().trim();
        String consumerPhone = form.getConsumerPhone().trim();
        String consumerAddress = form.getConsumerAddress().trim();

        //check whether input is empty
        if (StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(consumerConsignee)
                || StringUtils.isEmpty(consumerPhone) || StringUtils.isEmpty(consumerAddress)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all the information");
            return result;
        }

        //create the the assign and save
        Assign assign = new Assign();
        assign.setQrcode(qrcode);
        assign.setConsumerConsignee(consumerConsignee);
        assign.setConsumerPhone(consumerPhone);
        assign.setConsumerAddress(consumerAddress);
        ResultData response = assignService.createAssign(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("Success to create the assign");
            return result;
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to create the assign");
            return result;
        }
    }

    //管理人员分配安装任务时触发,修改任务状态,队伍,人员
    @RequestMapping(method = RequestMethod.POST, value = "/allocate")
    public ResultData allocate(AllocateForm form) {
        ResultData result = new ResultData();

        //check empty input
        if (StringUtils.isEmpty(form.getAssignId()) || StringUtils.isEmpty(form.getAssignId().trim()) || StringUtils.isEmpty(form.getTeamName()) || StringUtils.isEmpty(form.getTeamName().trim())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all information");
            return result;
        }

        String assignId = form.getAssignId().trim();
        String teamName = form.getTeamName().trim();
        String memberName = null;
        String installTime = null;
        if(form.getMemberName() != null)
            memberName = form.getMemberName().trim();
        if(form.getInstallDate() != null)
            installTime = form.getInstallDate().trim();

        //according to the teamName,find the teamId.
        String teamId = "";
        Map<String, Object> condition = new HashMap<>();
        condition.put("teamName", teamName);
        condition.put("blockFlag", false);
        ResultData response = teamService.fetchTeam(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            teamId = ((List<Team>) response.getData()).get(0).getTeamId();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no teamName found");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        //according to the memberName,find the memberId.
        String memberId = "";
        condition.clear();
        condition.put("teamId", teamId);
        condition.put("memberName", memberName);
        condition.put("blockFlag", false);
        response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            memberId = ((List<Member>) response.getData()).get(0).getMemberId();
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
            return result;
        }

        //according to the assignId,find the assign record.
        Assign assign = new Assign();
        condition.clear();
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
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            assign = ((List<Assign>) response.getData()).get(0);
        }

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
            result.setDescription("success to update assign");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to update assign");
        }
        return result;
    }

    //管理员查看所有安装任务时触发,拉取所有安装任务列表.
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData list() {
        ResultData result = new ResultData();

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no assign found");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now,please try again later");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
            result.setDescription("success to get the assign list");
        }
        return result;
    }

    //管理员查看待分配安装任务时触发,拉取待分配的安装任务列表.
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

    //由order模块调用触发,通过qrcode查询安装单
    @RequestMapping(method = RequestMethod.GET, value = "/byqrcode")
    public ResultData byqrcode(String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("qrcode", qrcode);
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
            result.setDescription("success to get the finished assign list by qrcode");
        }
        return result;
    }
}
