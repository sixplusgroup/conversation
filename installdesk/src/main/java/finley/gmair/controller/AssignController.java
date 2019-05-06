package finley.gmair.controller;

import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.installation.*;
import finley.gmair.model.message.MessageTemplate;
import finley.gmair.pool.InstallPool;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.SerialUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private AssignActionService assignActionService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private AssignSnapshotService assignSnapshotService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private AssignCodeService assignCodeService;

    @Autowired
    private ConfigService configService;

    /**
     * 根据表单中的姓名、电话、地址信息创建安装任务
     *
     * @param form
     * @return
     */
    @PostMapping("/create")
    public ResultData create(AssignForm form) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(form.getConsumerConsignee()) || StringUtils.isEmpty(form.getConsumerPhone()) || StringUtils.isEmpty(form.getConsumerAddress()) || StringUtils.isEmpty(form.getModel()) || StringUtils.isEmpty(form.getSource())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保安装客户的信息录入完整");
            return result;
        }
        //根据表单内容构建安装任务
        String consignee = form.getConsumerConsignee().trim();
        String phone = form.getConsumerPhone().trim();
        String address = form.getConsumerAddress().trim();
        String detail = form.getModel().trim();
        String source = form.getSource().trim();
        Assign assign;
        if (StringUtils.isEmpty(form.getDescription())) {
            assign = new Assign(consignee, phone, address, detail, source);
        } else {
            assign = new Assign(consignee, phone, address, detail, source, form.getDescription());
        }
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
            //记录安装任务操作日志
            InstallPool.getLogExecutor().execute(() -> {
                Assign a = (Assign) response.getData();
                AssignAction action = new AssignAction(a.getAssignId(), "系统生成新的安装任务");
                assignActionService.create(action);
            });
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
            result.setData(((List) response.getData()).get(0));
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
            //记录安装任务操作日志
            InstallPool.getLogExecutor().execute(() -> {
                condition.clear();
                condition.put("teamId", teamId);
                condition.put("blockFlag", false);
                ResultData r = teamService.fetch(condition);
                Team team = ((List<Team>) r.getData()).get(0);
                AssignAction action = new AssignAction(assignId, "分派安装任务到" + team.getTeamName() + "团队");
                assignActionService.create(action);
            });
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
            //记录安装任务操作日志
            InstallPool.getLogExecutor().execute(() -> {
                condition.clear();
                condition.put("memberId", memberId);
                condition.put("blockFlag", false);
                ResultData r = memberService.fetch(condition);
                Member member = ((List<Member>) r.getData()).get(0);
                AssignAction action = new AssignAction(assignId, "分派安装任务给安装工人: " + member.getMemberName());
                assignActionService.create(action);
                //获取安装任务信息
                condition.clear();
                condition.put("assignId", assignId);
                condition.put("blockFlag", false);
                r = assignService.fetch(condition);
                if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
                Assign assign = ((List<Assign>) r.getData()).get(0);
                //判断是否要发送短信
                condition.clear();
                condition.put("configComp", "message");
                condition.put("status", true);
                condition.put("blockFlag", false);
                r = configService.fetch(condition);
                if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
                //获取短信模板
                r = messageService.template("NOTIFICATION_DISPATCHED");
                if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
                MessageTemplate template = ((List<MessageTemplate>) r.getData()).get(0);
                String candidate = template.getMessage().replaceAll("###", "%s");
                candidate = String.format(candidate, assign.getDetail(), member.getMemberName(), member.getMemberPhone());
                //发送短信
                messageService.send(assign.getConsumerPhone(), candidate);
            });
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
        condition.put("teamId", null);
        condition.put("memberId", null);
        condition.put("assignStatus", AssignStatus.TODOASSIGN.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            InstallPool.getLogExecutor().execute(() -> {
                AssignAction action = new AssignAction(assignId, "系统召回了安装任务, 原因为" + message);
                assignActionService.create(action);
            });
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("召回安装任务失败，请重新尝试");
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
    public ResultData cancel(String assignId, String message) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(message)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务的信息和取消的原因");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.CLOSED.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setDescription("撤销安装任务成功");
            InstallPool.getLogExecutor().execute(() -> {
                AssignAction action = new AssignAction(assignId, "系统取消了安装任务, 原因为" + message);
                assignActionService.create(action);
            });
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
        condition.put("blockFlag", false);
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
        condition.put("blockFlag", false);
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
        condition.put("blockFlag", false);
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

    @GetMapping("/trace")
    public ResultData trace(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务相关的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = assignActionService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("未能查询到和该安装任务相关的处理信息");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询处理信息失败，请稍后尝试");
        }
        return result;
    }

    /**
     * 安装任务关联设备二维码
     *
     * @param assignId
     * @param qrcode
     * @return
     */
    @PostMapping("/init")
    public ResultData init(String assignId, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息和二维码信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("codeValue", qrcode);
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("二维码与安装任务关联失败，请稍后尝试");
            return result;
        }
        //记录安装任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "该安装任务使用二维码为" + qrcode + "的机器");
            assignActionService.create(action);
        });
        return result;
    }

    @PostMapping("/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture) || wifi == null || StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        Snapshot snapshot = new Snapshot(assignId, qrcode, picture, wifi, method, description);
        //存储安装快照
        ResultData response = assignSnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建安装任务快照失败，请稍后尝试");
            return result;
        }
        //更新安装任务状态
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("安装任务状态更改失败，请稍后尝试");
            return result;
        }
        //记录安装任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "安装完成，使用" + method + ", 网络" + ((wifi) ? "已配置" : "未配置") + (StringUtils.isEmpty(description) ? "" : ", 备注信息: " + description));
            assignActionService.create(action);
            //获取安装任务信息
            condition.clear();
            condition.put("assignId", assignId);
            condition.put("blockFlag", false);
            ResultData r = assignService.fetch(condition);
            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
            Assign assign = ((List<Assign>) r.getData()).get(0);
            //判断是否要发送短信
            condition.clear();
            condition.put("configComp", "message");
            condition.put("status", true);
            condition.put("blockFlag", false);
            r = configService.fetch(condition);
            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
            //获取短信模板
            r = messageService.template("NOTIFICATION_INSTALL");
            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) return;
            MessageTemplate template = ((List<MessageTemplate>) r.getData()).get(0);
            String candidate = template.getMessage().replaceAll("###", "%s");
            String code = SerialUtil.serial(4);
            //存储安装服务码
            AssignCode ac = new AssignCode(assignId, code);
            r = assignCodeService.create(ac);
            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("Fail to create code for assign");
                return;
            }
            candidate = String.format(candidate, assign.getDetail(), code);
            //发送短信
            messageService.send(assign.getConsumerPhone(), candidate);
        });
        result.setDescription("安装任务完成");
        return result;
    }

    /**
     * 获取安装任务的快照
     *
     * @param assignId
     * @return
     */
    @GetMapping("/snapshot")
    public ResultData snapshot(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("blockFlag", false);
        ResultData response = assignSnapshotService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("获取安装任务快照失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前任务尚无安装任务快照");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 安装工人提交服务码
     *
     * @param assignId
     * @param code
     * @return
     */
    @PostMapping("/eval")
    public ResultData eval(String assignId, String code) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("codeSerial", code);
        condition.put("blockFlag", false);
        //调用查询是否存在此服务码和此assignId的记录
        ResultData response = assignCodeService.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("服务码查询异常，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("服务码错误，请重新输入");
            return result;
        }
        //更新安装任务的状态
        condition.clear();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.EVALUATED.getValue());
        condition.put("blockFlag", false);
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("安装任务状态更新异常，请稍后尝试");
            return result;
        }
        return result;
    }
}
