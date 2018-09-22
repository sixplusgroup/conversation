package finley.gmair.controller;

import finley.gmair.form.installation.InstallDateForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.Member;
import finley.gmair.service.AssignService;
import finley.gmair.service.AssignServiceAgent;
import finley.gmair.service.FeedbackService;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/install-mp/assign")
public class AssignController {
    @Autowired
    private AssignService assignService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private AssignServiceAgent assignServiceAgent;

    @Autowired
    private FeedbackService feedbackService;

    //工人选择安装时间并提交时触发,修改任务状态,安装日期

    /**
     * This method is called when the member specified the installation date
     * A text message will be sent to notify the customer when this method is called
     *
     * @param form
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/date")
    public ResultData date(InstallDateForm form) {
        ResultData result = new ResultData();

        //check empty input
        if (StringUtils.isEmpty(form.getAssignId()) || StringUtils.isEmpty(form.getInstallDate())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please provide all required information");
            return result;
        }

        String assignId = form.getAssignId().trim();
        String installDate = form.getInstallDate().trim();

        //according to the assignId,find the assign record
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No install assign with id: ").append(assignId).append(" found").toString());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find install assign");
            return result;
        }
        Assign assign = ((List<Assign>) response.getData()).get(0);

        //update the assign
        if (assign.getAssignStatus().getValue() < AssignStatus.PROCESSING.getValue()) {
            assign.setAssignDate(Timestamp.valueOf(installDate));
            assign.setAssignStatus(AssignStatus.PROCESSING);
            response = assignService.updateAssign(assign);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                result.setData(response.getData());
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success to update date");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("fail to update date");
            }
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("can not update the processing or finished assign record");
        }
        return result;
    }

    /**
     * @param wechatId
     * @param status
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public ResultData assign(String wechatId, int status) {
        ResultData result = new ResultData();

        if (StringUtils.isEmpty(wechatId) || StringUtils.isEmpty(status)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }

        wechatId = wechatId.trim();
        //fetch member by wechat id

        Map<String, Object> condition = new HashMap<>();
        condition.put("wechatId", wechatId);
        condition.put("blockFlag", false);
        ResultData response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No member with openid: ").append(wechatId).append(" found").toString());
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy now");
            return result;
        }

        String memberId = ((List<Member>) response.getData()).get(0).getMemberId();

        condition.clear();

        //fetch the assigned assign list.
        condition.put("assignStatus", status);
        condition.put("memberId", memberId);
        condition.put("blockFlag", false);
        response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No install assign found with status: ").append(status).append(" for member: ").append(memberId).toString());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find install assign");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    /**
     * This method is used to fetch assigned tasks for a team
     *
     * @param teamId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/team/todo")
    public ResultData teamTodo(String teamId) {
        ResultData result = new ResultData();

        //check empty input
        if (StringUtils.isEmpty(teamId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Please make sure all required information is provided");
            return result;
        }

        teamId = teamId.trim();

        //fetch the assign list
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignStatus", AssignStatus.ASSIGNED.getValue());
        condition.put("teamId", teamId);
        condition.put("blockFlag", false);

        ResultData response = assignService.fetchAssign(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription(new StringBuffer("No assign found for team id: ").append(teamId).toString());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to find assign");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }

        return result;
    }

    //工人扫二维码时触发,查找assign表中是否存在qrcode为某值的记录
    @RequestMapping(method = RequestMethod.GET, value = "/qrcode")
    public ResultData qrcode(String qrcode) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("codeValue", qrcode);
        condition.put("blockFlag", false);
        ResultData response = assignService.fetchAssign(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("no such assign found");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("fail to find the assign with qrcode");
            return result;
        }
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(((List<Assign>) response.getData()).get(0).getAssignId());

        //检查该工人是否能安装这台机器
        String memberId = ((List<Assign>) response.getData()).get(0).getMemberId();
        String memberPhone = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        condition.clear();
        condition.put("memberPhone", memberPhone);
        response = memberService.fetchMember(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("can not find memberPhone");
            return result;
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("server is busy");
            return result;
        } else {
            Member member = ((List<Member>) response.getData()).get(0);
            if ((member.getMemberId()).equals(memberId)) {
                result.setResponseCode(ResponseCode.RESPONSE_OK);
                result.setDescription("success!");
            } else {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("you could not help others to install.");
            }
        }
        return result;
    }

    /**
     * This method postpone the installation assign
     * For each assign postponed, the current assignment will be marked as postponed / closed
     * A new installation assign will be generated.
     *
     * @param assignId
     * @param date
     * @return
     */
    @PostMapping("/postpone")
    public ResultData postpone(String assignId, String date) {
        //todo
        return assignServiceAgent.postpone(assignId, date);
    }

    @PostMapping("/cancel")
    public ResultData cancel(String assignId, String description) {
        ResultData result = new ResultData();
        ResultData response = assignServiceAgent.cancel(assignId, description);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        response = feedbackService.createFeedback(assignId, description);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(response.getDescription());
            return result;
        }
        return result;
    }
}
