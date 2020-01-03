package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.installation.AssignForm;
import finley.gmair.form.installation.CompanyForm;
import finley.gmair.model.installation.*;
import finley.gmair.model.resource.FileMap;
import finley.gmair.pool.InstallPool;
import finley.gmair.service.*;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultData list(String status, String teamId, Integer start, Integer length, String search) {
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
                response = assignService.principal(condition);
            } else response = assignService.fetch(condition);
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
                logger.info("template message: " + JSON.toJSONString(r));
                JSONObject json = JSONObject.parseObject(JSON.toJSONString(r));
                if (!json.getString("responseCode").equalsIgnoreCase("RESPONSE_OK")) return;
                json = json.getJSONArray("data").getJSONObject(0);
                String template = json.getString("message");
                String candidate = template.replaceAll("###", "%s");
                candidate = String.format(candidate, assign.getDetail(), member.getMemberName(), member.getMemberPhone());
                logger.info("message content: " + candidate);
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
    public ResultData tasks(String memberId, Integer status, String search, String page, String pageLength,String reverse) {
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
        if (!StringUtils.isEmpty(search)) {
            String fuzzysearch = "%" + search + "%";
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (pattern.matcher(search).matches()) {//如果search为数字
                condition.put("phone", fuzzysearch);
            } else {
                condition.put("consumer", fuzzysearch);
            }
            response = assignService.principal(condition);
            List<Assign> resultList = (List<Assign>) response.getData();
            if(!StringUtils.isEmpty(reverse)&&reverse.equals("true")){
                Collections.reverse(resultList);
            }
            int totalPage = resultList.size();
            result.setResponseCode(response.getResponseCode());
            JSONObject json = new JSONObject();
            json.put("totalPage", totalPage);
            json.put("list", resultList);
            result.setData(json);
        } else {
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
            if(!StringUtils.isEmpty(page)&&!StringUtils.isEmpty(pageLength)){
                List<Assign> resultList = (List<Assign>) response.getData();
                if(!StringUtils.isEmpty(reverse)&&reverse.equals("true")){
                    Collections.reverse(resultList);
                }
                int totalPage = resultList.size();
                if(totalPage>(Integer.parseInt(page)-1)*Integer.parseInt(pageLength)){
                    int total = Integer.parseInt(page)*Integer.parseInt(pageLength)>totalPage?totalPage:Integer.parseInt(page)*Integer.parseInt(pageLength);
                    resultList = resultList.subList((Integer.parseInt(page)-1)*Integer.parseInt(pageLength),total);
                }else {
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("查询为空");
                    return result;
//                    resultList = resultList.subList((Integer.parseInt(page)-1)*Integer.parseInt(pageLength),totalPage-1);
                }
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("totalPage", totalPage);
                jsonObject.put("list", resultList);
                result.setData(jsonObject);
//                result.setData(resultList);
            }else {
                result.setData(response.getData());
            }

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
    public ResultData overview(String memberId, Integer status, String search) {
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

    @PostMapping("/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description, String date) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture) || wifi == null || StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        //检测图片是否已存在
        String[] urls = picture.split(",");
        List<String> md5s=new ArrayList<>();
        List<String> newUrls=new ArrayList<>();
        ResultData response = null;
        Map<String, Object> md5_condition = new HashMap<>();
        for(int i = 0; i<urls.length;i++){
            response = resourcesService.getTempFileMap(urls[i]);
            if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
                String md5 = (String) response.getData();
                newUrls.add(urls[i]);
                md5_condition.clear();
                md5_condition.put("md5",md5);
                md5_condition.put("blockFlag",false);
                response = pictureMd5Service.fetch(md5_condition);
                if(response.getResponseCode()==ResponseCode.RESPONSE_NULL){
                    md5s.add(md5);
                }else if(response.getResponseCode()==ResponseCode.RESPONSE_OK){
                    result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                    result.setDescription("请勿上传重复图片");
                    return result;
                }
            }
        }
        for (int i=0;i<md5s.size();i++){
            pictureMd5Service.create(new PictureMd5(newUrls.get(i),md5s.get(i)));
        }
        picture = StringUtils.join(newUrls,",");
        Snapshot snapshot = new Snapshot(assignId, qrcode, picture, wifi, method, description);
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
    public ResultData report_query(String assignId, String teamId, String memberId, String beginTime, String endTime) {
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
        condition.put("blockFlag", false);
        ResultData response = assignService.report_fetch(condition);
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
     * @param assignId
     * @return
     */
    @PostMapping("/receive")
    public ResultData receive(String assignId){
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
     * @param assignId
     * @return
     */
    @PostMapping("/restore")
    public ResultData restore(String assignId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(assignId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供assignId");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        //删除snapshot
        condition.put("blockFlag",false);
        condition.put("assignId",assignId);
        ResultData response = assignSnapshotService.fetch(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        Snapshot snapshot = ((List<Snapshot>)response.getData()).get(0);
        String snapshotId = snapshot.getSnapshotId();
        response = assignSnapshotService.block(snapshotId);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("删除快照失败");
            return result;
        }
        //恢复任务为进行中
        condition.clear();
        condition.put("assignId",assignId);
        condition.put("assignStatus",AssignStatus.PROCESSING.getValue());
        response = assignService.update(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
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
     * @param form
     * @return
     */
    @PostMapping("/company/create")
    ResultData createCompany(CompanyForm form){
        ResultData result = new ResultData();
        Company company = new Company(form.getCompanyName(),form.getMessageTitle(),form.getCompanyDetail());
        ResultData response = companyService.create(company);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("创建失败");
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 获取公司列表
     * @return
     */
    @GetMapping("/company/list")
    ResultData getCompanyList(){
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        ResultData response = companyService.fetch(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据companyId查询公司详情
     * @param companyId
     * @return
     */
    @GetMapping("/company/query")
    ResultData getCompanyById(String companyId){
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("companyId",companyId);
        condition.put("blockFlag",false);
        ResultData response = companyService.fetch(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
