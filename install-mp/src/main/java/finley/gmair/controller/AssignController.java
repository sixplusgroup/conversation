package finley.gmair.controller;

import finley.gmair.service.AssignService;
import finley.gmair.service.ResourceService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @ClassName: AssignController
 * @Description: TODO
 * @Author fan
 * @Date 2019/4/9 4:21 PM
 */
@RestController
@RequestMapping("/install-mp/assign")
public class AssignController {
    private Logger logger = LoggerFactory.getLogger(AssignController.class);

    @Autowired
    private AssignService assignService;

    @Autowired
    private ResourceService resourceService;

    /**
     * 安装负责人查看安装任务
     *
     * @param memberId
     * @return
     */
    @GetMapping("/tasks")
    public ResultData assigns(String memberId, Integer status, String search) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装负责人的信息");
            return result;
        }
        result = assignService.fetchAssign(memberId, status, search);
        return result;
    }

    @PostMapping("/alloc")
    public ResultData allocate(String assignId, String memberId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务和成员信息");
            return result;
        }
        result = assignService.dispatchAssign(assignId, memberId);

        return result;
    }

    @GetMapping("/own")
    public ResultData overview(String memberId, Integer status, String search) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装工人的信息");
            return result;
        }
        result = assignService.fetchOwnAssign(memberId, status, search);
        return result;
    }

    /**
     * 安装负责人召回安装任务
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
        result = assignService.recallAssign(assignId, message);
        return result;
    }

    /**
     * 安装负责人取消安装任务
     *
     * @param assignId
     * @return
     */
    @PostMapping("/cancel")
    public ResultData cancel(String assignId, String message) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(message)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息");
            return result;
        }
        result = assignService.cancelAssign(assignId, message);
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
        result = assignService.traceAssign(assignId);
        return result;
    }

    /**
     * 安装工人将二维码与安装任务进行关联
     *
     * @return
     */
    @PostMapping("/init")
    public ResultData init(String assignId, String qrcode) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息和二维码");
            return result;
        }
        result = assignService.initAssign(assignId, qrcode);
        return result;
    }

    /**
     * 安装工人提交安装任务表单
     *
     * @param assignId
     * @param qrcode
     * @param picture
     * @param wifi
     * @param method
     * @param description
     * @return
     */
    @PostMapping("/submit")
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(qrcode) || StringUtils.isEmpty(picture) || wifi == null || StringUtils.isEmpty(method)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装快照相关的信息");
            return result;
        }
        //提交安装图片资源
        result = resourceService.save(picture);
        if (result.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            return result;
        }
        if (StringUtils.isEmpty(description)) {
            result = assignService.submitAssign(assignId, qrcode, picture, wifi, method);
        } else {
            result = assignService.submitAssign(assignId, qrcode, picture, wifi, method, description);
        }
        return result;
    }

    /**
     * 查看安装任务的快照
     *
     * @param assignId
     * @return
     */
    @GetMapping("/snapshot")
    public ResultData snapshot(String assignId) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(assignId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务信息");
            return result;
        }
        result = assignService.snapshotAssign(assignId);
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
        if (StringUtils.isEmpty(assignId) || StringUtils.isEmpty(code)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装任务及服务码信息");
            return result;
        }
        result = assignService.evalAssign(assignId, code);
        return result;
    }
}