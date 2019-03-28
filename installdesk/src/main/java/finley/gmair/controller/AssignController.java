package finley.gmair.controller;

import finley.gmair.form.installation.AssignForm;
import finley.gmair.model.installation.Assign;
import finley.gmair.service.AssignService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.HashMap;
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
    public ResultData assign() {
        ResultData result = new ResultData();

        return result;
    }

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
}
