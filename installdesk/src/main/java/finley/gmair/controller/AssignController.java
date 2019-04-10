package finley.gmair.controller;

import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.AssignStatus;
import finley.gmair.model.installation.TeamWatch;
import finley.gmair.service.AssignService;
import finley.gmair.service.MemberService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: AssignController
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/22 4:49 PM
 */
@RestController
@RequestMapping("/install/assign")
public class AssignController {
    private Logger logger = LoggerFactory.getLogger(AssignController.class);

    @Autowired
    private AssignService assignService;

    @Autowired
    private MemberService memberService;

    /**
     * 根据表单中的姓名、电话、地址信息创建安装任务
     *
     * @param form
     * @return
     */
    @PostMapping("/create")
    public ResultData create(AssignForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getConsumerConsignee()) || StringUtils.isEmpty(form.getConsumerPhone()) || StringUtils.isEmpty(form.getConsumerAddress()) || StringUtils.isEmpty(form.getModel())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保安装客户的信息录入完整");
            return result;
        }
        //根据表单内容构建安装任务
        String consignee = form.getConsumerConsignee().trim();
        String phone = form.getConsumerPhone().trim();
        String address = form.getConsumerAddress().trim();
        String detail = form.getModel().trim();
        Assign assign = new Assign(consignee, phone, address, detail);
        //若上传了二维码，则安装必须为该指定设备
        String qrcode = form.getQrcode();
        if (StringUtils.isEmpty(qrcode)) {
            assign.setCodeValue(qrcode);
        }
        //存入数据库
        ResultData response = assignService.create(assign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建安装任务失败，请稍后尝试");
        }
        return result;
    }

    /**
     * 获取安装任务列表，可根据团队或者任务的进行状态进行筛选
     *
     * @param status
     * @return
     */
    @GetMapping("/list")
    public ResultData list(String status, String teamId, Integer start, Integer length) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(status)) {
            condition.put("assignStatus", status);
        }
        if (!StringUtils.isEmpty(teamId)) {
            condition.put("teamId", teamId);
        }
        condition.put("blockFlag", false);
        ResultData response;
        if (start == null || length == null) {
            response = assignService.fetch(condition);
        } else {
            response = assignService.fetch(condition, start, length);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有符合条件的安装任务");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装任务失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        return result;
    }

    @GetMapping("/{assignId}/info")
    public ResultData detail(@PathVariable("assignId") String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入任务的编号");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = assignService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有符合条件的安装任务");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装任务失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            response.setData(((List) response.getData()).get(0));
        }
        return result;
    }

    /**
     * 调度人员分配安装工单
     *
     * @return
     */
    @PostMapping("/dispatch")
    public ResultData dispatch(String assignId, String teamId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("teamId", teamId);
        condition.put("assignStatus", AssignStatus.ASSIGNED.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("分配安装任务失败，请重新尝试");
        }
        return result;
    }

    /**
     * 安装负责人分配订单到安装工人
     *
     * @return
     */
    @PostMapping("/assign")
    public ResultData assign(String assignId, String memberId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("memberId", memberId);
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("分配安装任务给安装工人失败，请重新尝试");
        }
        return result;
    }

    /**
     * 安装负责人召回安装任务，安装任务召回后，将回到初始状态，需要由调度人员重新进行派单
     * 仅可对出于Assigned和Processing状态的订单进行召回，召回时需要提供原因
     *
     * @param assignId
     * @return
     */
    @PostMapping("/recall")
    public ResultData recall(String assignId, String message) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(message)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息和召回原因");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("teamId", "");
        condition.put("memberId", "");
        condition.put("assignStatus", AssignStatus.TODOASSIGN.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("分配安装任务给安装工人失败，请重新尝试");
        }
        return result;
    }

    /**
     * 调度人员撤销安装任务
     *
     * @param assignId
     * @return
     */
    @PostMapping("/cancel")
    public ResultData cancel(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保请求所需要的参数已设置");
            return result;
        }
        ResultData response = assignService.block(assignId);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("撤销安装任务成功");
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("撤销安装任务失败，请重新尝试");
        }
        return result;
    }

    /**
     * 安装负责人查看所有订单
     *
     * @return
     */
    @GetMapping("/tasks")
    public ResultData tasks(String memberId, Integer status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装负责人的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberId", memberId);
        //根据memberid获取关注的团队
        ResultData response = memberService.fetchTeams(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询该安装负责人信息时出错，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("该安装负责人暂无负责的团队信息");
            return result;
        }
        List<TeamWatch> list = (List<TeamWatch>) response.getData();
        List<String> teams = new ArrayList<>();
        for (TeamWatch tw : list) {
            teams.add(tw.getTeamId());
        }
        condition.clear();
        condition.put("teams", teams);
        if (status != null) {
            condition.put("assignStatus", status);
        }
        response = assignService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装负责人所负责的安装任务失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未查询到相关的安装任务");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 安装工人查看所有的订单
     *
     * @param memberId
     * @return
     */
    @GetMapping("/overview")
    public ResultData overview(String memberId, Integer status) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装工人的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("memberId", memberId);
        if (status != null) {
            condition.put("assignStatus", status);
        }
        ResultData response = assignService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装任务失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前暂无任何安装任务，请稍后尝试");
        }
        result.setData(response.getData());
        return result;
    }
}
