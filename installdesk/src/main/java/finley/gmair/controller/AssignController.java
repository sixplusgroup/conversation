package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.converters.Auto;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.form.installation.CompanyForm;
import finley.gmair.model.installation.*;
import finley.gmair.pool.InstallPool;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Pattern;

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

    @Autowired
    private PictureMd5Service pictureMd5Service;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private FixSnapshotService fixSnapshotService;

    @Autowired
    private SurveySnapshotService surveySnapshotService;

    @Autowired
    private ChangeMachineSnapshotService changeMachineSnapshotService;

    @Autowired
    private DisassembleSnapshotService disassembleSnapshotService;

    @Autowired
    private UserassignService userassignService;

    @Resource
    private AssignTypeInfoService assignTypeInfoService;

//    @RequestMapping("/test")
//    public String test(){
//        return "ok";
//    }


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
        // 检查前端传入的工单类型是否合法
        if (!assignTypeInfoService.isValidType(form.getType())) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请确保工单类型填写正确");
            return result;
        }
        //根据表单内容构建安装任务
        String consignee = form.getConsumerConsignee().trim();
        String phone = form.getConsumerPhone().trim();
        String address = form.getConsumerAddress().trim();
        String detail = form.getModel().trim();
        String source = form.getSource().trim();
        String type = form.getType();

        Assign assign;
        if (!StringUtils.isEmpty(form.getCompany())) {
            assign = new Assign(consignee, phone, address, detail, source, form.getDescription(), form.getCompany().trim(),type);
        } else {
            assign = new Assign(consignee, phone, address, detail, source, form.getDescription(),null,type);
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
    public ResultData list(String status, String teamId, Integer curPage, Integer length, String search, String sortType) {
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
        if (!StringUtils.isEmpty(search)) {
            //删除对于订单状态的选择
            condition.remove("assignStatus");
            String fuzzysearch = "%" + search + "%";
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            //如果搜索内容为数字
            if (pattern.matcher(search).matches()) {
                condition.put("phone", fuzzysearch);
            } else {
                condition.put("consumer", fuzzysearch);
            }
        }

        if (!StringUtils.isEmpty(sortType)) {
            condition.put("sortType", sortType);
        }
        if (curPage == null || length == null) {
            response = assignService.principal(condition);
        } else {
            int start = (curPage - 1) * length;
            response = assignService.principal(condition, start, length);
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
        condition.put("blockFlag", false);
        ResultData response = assignService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        //获取signature信息
        String companyId = ((List<Assign>) response.getData()).get(0).getCompanyId();
        response = getCompanyById(companyId);
        String signature;
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            signature = "";
        } else {
            signature = ((List<Company>) response.getData()).get(0).getMessageTitle();
        }

        condition.clear();
        condition.put("assignId", assignId);
        condition.put("memberId", memberId);
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        response = assignService.update(condition);
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
                logger.info("template message: " + JSON.toJSONString(r));
                JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
                if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
                json = json.getJSONArray("data").getJSONObject(0);
                String template = json.getString("message");
                String candidate = template.replaceAll("###", "%s");
                candidate = String.format(candidate, assign.getDetail(), member.getMemberName(), member.getMemberPhone());
                logger.info("message content: " + candidate);
                //发送短信
                if (signature.equals("")) {
                    messageService.send(assign.getConsumerPhone(), candidate);
                } else {
                    messageService.send(assign.getConsumerPhone(), candidate, signature);
                }
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
    public ResultData tasks(String memberId, Integer status, String search, String page, String pageLength, String reverse, String sortType) {
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
        if (reverse.equals("true")) {
            reverse = "desc";
        } else {
            reverse = "asc";
        }
        condition.put("reverse", reverse);
        if (status != null) {
            condition.put("assignStatus", status);
        }
        if (sortType != null && !sortType.isEmpty()){
            condition.put("sortType", sortType);
        }
        if (!StringUtils.isEmpty(search)) {
            String fuzzysearch = "%" + search + "%";
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(search).matches()) {//如果search为数字
                condition.put("phone", fuzzysearch);
            } else {
                condition.put("consumer", fuzzysearch);
            }
            response = assignService.principal(condition, (Integer.parseInt(page) - 1) * Integer.parseInt(pageLength), Integer.parseInt(pageLength));
            result.setResponseCode(response.getResponseCode());
            result.setData(response.getData());
        } else {
            response = assignService.principal(condition, (Integer.parseInt(page) - 1) * Integer.parseInt(pageLength), Integer.parseInt(pageLength));
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
        }
        return result;
    }


    /**
     * 安装工人查看所有的订单
     *
     * @param memberId
     * @return
     */
    @GetMapping("/overview")
    public ResultData overview(String memberId, Integer status, String search, String sortType) {
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
        if (!StringUtils.isEmpty(sortType)) {
            condition.put("sortType", sortType);
        }
        ResultData response;
        if (!StringUtils.isEmpty(search)) {
            String fuzzysearch = "%" + search + "%";
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(search).matches()) {//如果search为数字
                condition.put("phone", fuzzysearch);
            } else {
                condition.put("consumer", fuzzysearch);
            }
            response = assignService.worker(condition);
        } else {
            response = assignService.fetch(condition);
        }
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

    /**
     * 提交安装任务相关信息
     *
     * @param assignId
     * @param qrcode
     * @param picture
     * @param wifi
     * @param method
     * @param description
     * @param date
     * @return
     */
    @PostMapping("/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description, String date, Integer hole) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture) || wifi == null || StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s = new ArrayList<>();
        List<String> newUrls = new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {
            response = resourcesService.getTempFileMap(urls[i]);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5", md5);
                md5_condition.put("blockFlag", false);
                response = pictureMd5Service.fetch(md5_condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    md5s.add(md5);
                } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i = 0; i < md5s.size(); i++) {
            pictureMd5Service.create(new PictureMd5(newUrls.get(i), md5s.get(i)));
        }
        picture = StringUtils.join(newUrls, ",");
        Snapshot snapshot = null;
        if (hole == null) {
            snapshot = new Snapshot(assignId, qrcode, picture, wifi, method, description);
        } else {
            snapshot = new Snapshot(assignId, qrcode, picture, wifi, method, description, hole);
        }
        //存储安装快照
        response = assignSnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建安装任务快照失败，请稍后尝试");
            return result;
        }
        //更新完成时间
        if (!StringUtils.isEmpty(date)) {
            Map<String, Object> install_assign_condition = new HashMap<>();
            install_assign_condition.put("assignDate", date);
            install_assign_condition.put("assignId", assignId);
            install_assign_condition.put("blockFlag", false);
            assignService.update(install_assign_condition);
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

            logger.info("template message: " + JSON.toJSONString(r));
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
            if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
            json = json.getJSONArray("data").getJSONObject(0);
            String template = json.getString("message");
            String candidate = template.replaceAll("###", "%s");
//            String code = SerialUtil.serial(4);
//            //存储安装服务码
//            AssignCode ac = new AssignCode(assignId, code);
//            r = assignCodeService.create(ac);
//            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("Fail to create code for assign");
//                return;
//            }
//            candidate = String.format(candidate, assign.getDetail(), code);
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

    @GetMapping("/report")
    public ResultData report_query(String assignId, String teamId, String memberId, String beginTime, String endTime, String sortType, Integer page, Integer pageLength) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(assignId)) {
            condition.put("assignId", assignId);
        }
        if (!StringUtils.isEmpty(teamId)) {
            condition.put("teamId", teamId);
        }
        if (!StringUtils.isEmpty(memberId)) {
            condition.put("memberId", memberId);
        }
        if (!StringUtils.isEmpty(beginTime)) {
            condition.put("beginTime", beginTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            condition.put("endTime", endTime);
        }
        if (!StringUtils.isEmpty(sortType)) {
            condition.put("sortType", sortType);
        }
        condition.put("blockFlag", false);
        ResultData response = null;
        if (page != null && pageLength != null){
            response = assignService.report_fetch(condition , (page-1)*pageLength, pageLength);
        }
        else {
            response = assignService.report_fetch(condition);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询异常，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前查询条件无记录");
            return result;
        }

        result.setData(response.getData());
        return result;
    }

    /**
     * 将安装任务状态改为已签收（待安装）等待安装人员安装
     *
     * @param assignId
     * @return
     */
    @PostMapping("/receive")
    public ResultData receive(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.RECEIVED.getValue());
        ResultData response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询该安装任务信息时出错，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("暂无该安装任务信息");
            return result;
        }
        result.setData(response.getData());
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }

    /**
     * 根据assignId恢复订单状态为进行中
     *
     * @param assignId
     * @return
     */
    @PostMapping("/restore")
    public ResultData restore(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供assignId");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        //删除snapshot
        condition.put("blockFlag", false);
        condition.put("assignId", assignId);
        ResultData response = assignSnapshotService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        Snapshot snapshot = ((List<Snapshot>) response.getData()).get(0);
        String snapshotId = snapshot.getSnapshotId();
        response = assignSnapshotService.block(snapshotId);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除快照失败");
            return result;
        }
        //恢复任务为进行中
        condition.clear();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.PROCESSING.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("恢复任务失败");
            return result;
        }
        AssignAction action = new AssignAction(assignId, "安装任务恢复为进行中");
        assignActionService.create(action);
        result.setData(response.getData());
        result.setDescription("恢复任务成功");
        return result;
    }

    /**
     * 创建服务公司
     *
     * @param form
     * @return
     */
    @PostMapping("/company/create")
    ResultData createCompany(CompanyForm form) {
        ResultData result = new ResultData();
        Company company = new Company(form.getCompanyName(), form.getMessageTitle(), form.getCompanyDetail());
        ResultData response = companyService.create(company);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("创建失败");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 获取公司列表
     *
     * @return
     */
    @GetMapping("/company/list")
    ResultData getCompanyList() {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData response = companyService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据companyId查询公司详情
     *
     * @param companyId
     * @return
     */
    @GetMapping("/company/query")
    ResultData getCompanyById(String companyId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("companyId", companyId);
        condition.put("blockFlag", false);
        ResultData response = companyService.fetch(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 提交维修任务相关信息
     *
     * @param assignId
     * @param qrcode
     * @param picture
     * @param description
     * @param date
     * @return
     */
    @PostMapping("/submit/fix")
    public ResultData submitFix(String assignId, String qrcode, String picture, String description, String date) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供维修快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s = new ArrayList<>();
        List<String> newUrls = new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {
            response = resourcesService.getTempFileMap(urls[i]);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5", md5);
                md5_condition.put("blockFlag", false);
                response = pictureMd5Service.fetch(md5_condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    md5s.add(md5);
                } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i = 0; i < md5s.size(); i++) {
            pictureMd5Service.create(new PictureMd5(newUrls.get(i), md5s.get(i)));
        }
        picture = StringUtils.join(newUrls, ",");
        SnapshotFix snapshot = new SnapshotFix(assignId, qrcode, picture, description);
        //存储维修快照
        response = fixSnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建维修任务快照失败，请稍后尝试");
            return result;
        }
        //更新完成时间
        if (!StringUtils.isEmpty(date)) {
            Map<String, Object> install_assign_condition = new HashMap<>();
            install_assign_condition.put("assignDate", date);
            install_assign_condition.put("assignId", assignId);
            install_assign_condition.put("blockFlag", false);
            assignService.update(install_assign_condition);
        }
        //更新维修任务状态
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("维修任务状态更改失败，请稍后尝试");
            return result;
        }
        //记录维修任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "维修完成" + (StringUtils.isEmpty(description) ? "" : ", 备注信息: " + description));
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

            logger.info("template message: " + JSON.toJSONString(r));
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
            if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
            json = json.getJSONArray("data").getJSONObject(0);
            String template = json.getString("message");
            String candidate = template.replaceAll("###", "%s");
//            String code = SerialUtil.serial(4);
//            //存储安装服务码
//            AssignCode ac = new AssignCode(assignId, code);
//            r = assignCodeService.create(ac);
//            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("Fail to create code for assign");
//                return;
//            }
//            candidate = String.format(candidate, assign.getDetail(), code);
            //发送短信
            messageService.send(assign.getConsumerPhone(), candidate);
        });
        result.setDescription("维修任务完成");
        return result;
    }

    /**
     * 提交勘测任务相关信息
     *
     * @param assignId
     * @param picture
     * @param description
     * @param date
     * @return
     */
    @PostMapping("/submit/survey")
    public ResultData submitSurvey(String assignId, String picture, String description, String date) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(picture)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供勘测快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s = new ArrayList<>();
        List<String> newUrls = new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {
            response = resourcesService.getTempFileMap(urls[i]);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5", md5);
                md5_condition.put("blockFlag", false);
                response = pictureMd5Service.fetch(md5_condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    md5s.add(md5);
                } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i = 0; i < md5s.size(); i++) {
            pictureMd5Service.create(new PictureMd5(newUrls.get(i), md5s.get(i)));
        }
        picture = StringUtils.join(newUrls, ",");
        SnapshotSurvey snapshot = new SnapshotSurvey(assignId, picture, description);
        //存储勘测快照
        response = surveySnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建勘测任务快照失败，请稍后尝试");
            return result;
        }
        //更新完成时间
        if (!StringUtils.isEmpty(date)) {
            Map<String, Object> install_assign_condition = new HashMap<>();
            install_assign_condition.put("assignDate", date);
            install_assign_condition.put("assignId", assignId);
            install_assign_condition.put("blockFlag", false);
            assignService.update(install_assign_condition);
        }
        //更新勘测任务状态
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("勘测任务状态更改失败，请稍后尝试");
            return result;
        }
        //记录勘测任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "勘测完成" + (StringUtils.isEmpty(description) ? "" : ", 备注信息: " + description));
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

            logger.info("template message: " + JSON.toJSONString(r));
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
            if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
            json = json.getJSONArray("data").getJSONObject(0);
            String template = json.getString("message");
            String candidate = template.replaceAll("###", "%s");
//            String code = SerialUtil.serial(4);
//            //存储安装服务码
//            AssignCode ac = new AssignCode(assignId, code);
//            r = assignCodeService.create(ac);
//            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("Fail to create code for assign");
//                return;
//            }
//            candidate = String.format(candidate, assign.getDetail(), code);
            //发送短信
            messageService.send(assign.getConsumerPhone(), candidate);
        });
        result.setDescription("勘测任务完成");
        return result;
    }

    /**
     * 提交换机任务相关信息
     *
     * @param assignId
     * @param oldQrcode
     * @param newQrcode
     * @param picture
     * @param wifi
     * @param description
     * @param date
     * @return
     */
    @PostMapping("/submit/changemachine")
    public ResultData submitChangeMachine(String assignId, String oldQrcode, String newQrcode, String picture, Boolean wifi, String description, String date, String wayBill) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(oldQrcode) || StringUtils.isEmpty(picture) || wifi == null || StringUtils.isEmpty(newQrcode) || StringUtils.isEmpty(wayBill)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供换机快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s = new ArrayList<>();
        List<String> newUrls = new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {
            response = resourcesService.getTempFileMap(urls[i]);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5", md5);
                md5_condition.put("blockFlag", false);
                response = pictureMd5Service.fetch(md5_condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    md5s.add(md5);
                } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i = 0; i < md5s.size(); i++) {
            pictureMd5Service.create(new PictureMd5(newUrls.get(i), md5s.get(i)));
        }
        picture = StringUtils.join(newUrls, ",");
        SnapshotChangeMachine snapshot = new SnapshotChangeMachine(assignId, oldQrcode, newQrcode, picture, wifi, description, wayBill);
        //存储换机快照
        response = changeMachineSnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建换机任务快照失败，请稍后尝试");
            return result;
        }
        //更新完成时间
        if (!StringUtils.isEmpty(date)) {
            Map<String, Object> install_assign_condition = new HashMap<>();
            install_assign_condition.put("assignDate", date);
            install_assign_condition.put("assignId", assignId);
            install_assign_condition.put("blockFlag", false);
            assignService.update(install_assign_condition);
        }
        //更新换机任务状态
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("换机任务状态更改失败，请稍后尝试");
            return result;
        }
        //记录换机任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "换机完成" + ", 网络" + ((wifi) ? "已配置" : "未配置") + (StringUtils.isEmpty(description) ? "" : ", 备注信息: " + description));
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

            logger.info("template message: " + JSON.toJSONString(r));
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
            if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
            json = json.getJSONArray("data").getJSONObject(0);
            String template = json.getString("message");
            String candidate = template.replaceAll("###", "%s");
//            String code = SerialUtil.serial(4);
//            //存储安装服务码
//            AssignCode ac = new AssignCode(assignId, code);
//            r = assignCodeService.create(ac);
//            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("Fail to create code for assign");
//                return;
//            }
//            candidate = String.format(candidate, assign.getDetail(), code);
            //发送短信
            messageService.send(assign.getConsumerPhone(), candidate);
        });
        result.setDescription("换机任务完成");
        return result;
    }

    /**
     * 提交拆机任务相关信息
     *
     * @param assignId
     * @param qrcode
     * @param picture
     * @param description
     * @param date
     * @param wayBill
     * @return
     */
    @PostMapping("submit/disassemble")
    public ResultData submitDisassemble(String assignId, String qrcode, String picture, String description, String date, String wayBill) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供拆机快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s = new ArrayList<>();
        List<String> newUrls = new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for (int i = 0; i < urls.length; i++) {
            response = resourcesService.getTempFileMap(urls[i]);
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5", md5);
                md5_condition.put("blockFlag", false);
                response = pictureMd5Service.fetch(md5_condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                    md5s.add(md5);
                } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i = 0; i < md5s.size(); i++) {
            pictureMd5Service.create(new PictureMd5(newUrls.get(i), md5s.get(i)));
        }
        picture = StringUtils.join(newUrls, ",");
        SnapshotDisassemble snapshot = new SnapshotDisassemble(assignId, qrcode, picture, description, wayBill);
        //存储换机快照
        response = disassembleSnapshotService.create(snapshot);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("创建换机任务快照失败，请稍后尝试");
            return result;
        }
        //更新完成时间
        if (!StringUtils.isEmpty(date)) {
            Map<String, Object> install_assign_condition = new HashMap<>();
            install_assign_condition.put("assignDate", date);
            install_assign_condition.put("assignId", assignId);
            install_assign_condition.put("blockFlag", false);
            assignService.update(install_assign_condition);
        }
        //更新换机任务状态
        Map<String, Object> condition = new HashMap<>();
        condition.put("assignId", assignId);
        condition.put("assignStatus", AssignStatus.FINISHED.getValue());
        response = assignService.update(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("换机任务状态更改失败，请稍后尝试");
            return result;
        }
        //记录维修任务操作日志
        InstallPool.getLogExecutor().execute(() -> {
            AssignAction action = new AssignAction(assignId, "换机完成" + (StringUtils.isEmpty(description) ? "" : ", 备注信息: " + description));
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

            logger.info("template message: " + JSON.toJSONString(r));
            JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
            if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
            json = json.getJSONArray("data").getJSONObject(0);
            String template = json.getString("message");
            String candidate = template.replaceAll("###", "%s");
//            String code = SerialUtil.serial(4);
//            //存储安装服务码
//            AssignCode ac = new AssignCode(assignId, code);
//            r = assignCodeService.create(ac);
//            if (r.getResponseCode() != ResponseCode.RESPONSE_OK) {
//                logger.error("Fail to create code for assign");
//                return;
//            }
//            candidate = String.format(candidate, assign.getDetail(), code);
            //发送短信
            messageService.send(assign.getConsumerPhone(), candidate);
        });
        result.setDescription("换机任务完成");
        return result;
    }

    /**
     * 查看选中工人所有订单情况 可按时间筛选，可按订单状态分类
     */
    @GetMapping("/order")
    public ResultData overviewNow(String memberId, String assignStatus,String duration, Integer curPage, Integer length) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装工人的信息");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("memberId", memberId);
        //assignStatus:TODOASSIGN(0)待分派, ASSIGNED(1)已分派, PROCESSING(2)处理中, FINISHED(3)已完成, CLOSED(4)已关闭, EVALUATED(5)已评价, RECEIVED(6)已签收（待安装）;
        if (!StringUtils.isEmpty(assignStatus)) {
            condition.put("assignStatus", assignStatus);
        }
        //duration:lastWeek 近7天,thisMonth 本月,thisYear 本年;
        if(duration!=null){
            condition.put("duration", duration);
        }
        int start = (curPage - 1) * length;
        ResultData response = assignService.principal(condition, start, length);
        result.setDescription("查询成功");
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询失败，请稍后尝试");
            return result;
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("没有符合条件的订单，请稍后尝试");
        }
        result.setData(response.getData());
        return result;
    }

    @GetMapping("/assignTypeInfo/all")
    public ResultData queryAllAssignTypeInfo() {
        ResultData res = new ResultData();
        res.setData(assignTypeInfoService.queryAll());
        return res;
    }

    @GetMapping("/assignTypeInfo/one")
    public ResultData queryAssignTypeInfoByType(@RequestParam String assignType) {
        ResultData res = new ResultData();
        AssignTypeInfo one = assignTypeInfoService.queryByAssignType(assignType);

        if (one == null || !assignType.equals(one.getAssignType())) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("无此工单类型！");
        }
        else {
            res.setData(one);
        }
        return res;
    }
    /**
     * 用户查询自己所有assign的状态
     */
    @PostMapping("/queryAssignState")
    public ResultData queryAssignState(@RequestParam String consumerPhone){
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(consumerPhone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请输入电话号码");
            return result;
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("consumerPhone", consumerPhone);
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
            result.setData(((List) response.getData()));
        }
        return result;
    }

    /**
     * 保存预约信息
     */
    @PostMapping("/reservation/save")
    public ResultData saveReservations(@RequestBody Userassign userassign){
        ResultData resultData = new ResultData();
        ResultData response = userassignService.insert(userassign);
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL||userassign==null) {
            resultData.setResponseCode(ResponseCode.RESPONSE_NULL);
            resultData.setDescription("数据不可为空");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROR);
            resultData.setDescription("保存失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            resultData.setResponseCode(ResponseCode.RESPONSE_OK);
//            resultData.setData(((Userassign) response.getData()));
            resultData.setDescription("预约信息保存成功");
        }

        return resultData;
    }
    /**
     * 获取预约信息
     */
    @GetMapping("/reservation/list")
    public ResultData reservationList(String status,  Integer curPage, Integer length, String search, String sortType){
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        if (!StringUtils.isEmpty(status)) {
            condition.put("userassignStatus", status);
        }
        condition.put("blockFlag", false);
        ResultData response;
        if (!StringUtils.isEmpty(search)) {
            //删除对于订单状态的选择
            condition.remove("userassignStatus");
            String fuzzysearch = "%" + search + "%";
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            //如果搜索内容为数字
            if (pattern.matcher(search).matches()) {
                condition.put("consumerPhone", fuzzysearch);
            } else {
                condition.put("consumerConsignee", fuzzysearch);
            }
        }

        if (!StringUtils.isEmpty(sortType)) {
            condition.put("sortType", sortType);
        }
        if (curPage == null || length == null) {
            response = userassignService.principal(condition);
        } else {
            int start = (curPage - 1) * length;
            response = userassignService.principal(condition, start, length);
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("当前没有符合条件的安装预约");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("查询安装预约失败，请稍后尝试");
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }

        return result;
    }

    /**
     * 预约确认
     */
    @PostMapping("/reservation/confirm")
    public ResultData reservationConfirm(@RequestParam String userassignId){
        ResultData result = new ResultData();
        ResultData response;
        if (!StringUtils.isEmpty(userassignId)) {
            response = userassignService.confirmReservation(userassignId);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("请提供需要确认的预约ID");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("确认安装预约失败，请稍后尝试");
            }
            Map<String, Object> condition = new HashMap<>();
            condition.put("userassignId",userassignId);
            response = userassignService.fetch(condition);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("确认安装预约失败，请稍后尝试");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("确认安装预约失败，请稍后尝试");
            }
            Userassign userassign = (Userassign)((List)response.getData()).get(0);
            Assign assign = new Assign(userassign.getConsumerConsignee(),userassign.getConsumerPhone(),userassign.getConsumerAddress(),userassign.getUserassignDetail());
            assign.setType(userassign.getUserassignType());
            assign.setDescription(userassign.getRemarks());
            response = assignService.create(assign);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("确认安装预约失败，请稍后尝试");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("确认安装预约失败，请稍后尝试");
            }

        }else{
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供需要确认的预约ID");
        }
        return result;

    }
    /**
     * 预约修改
     */
    @PostMapping("/reservation/adjust")
    public ResultData reservationAdjust(@RequestBody Userassign userassign){
        ResultData result = new ResultData();
        ResultData response;
        if (!StringUtils.isEmpty(userassign.getUserassignId())) {
            response = userassignService.adjust(userassign);
            if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
                result.setResponseCode(ResponseCode.RESPONSE_NULL);
                result.setDescription("修改安装预约失败，请稍后尝试");
            } else if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("修改安装预约失败，请稍后尝试");
        }
        }else{
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("请提供需要修改的预约ID");
        }
        return result;
    }
}
