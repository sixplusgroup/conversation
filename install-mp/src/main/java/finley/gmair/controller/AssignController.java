package finley.gmair.controller;

import finley.gmair.model.installation.Assign;
import finley.gmair.model.installation.Userassign;
import finley.gmair.service.AssignService;
import finley.gmair.service.ResourceService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

//    @RequestMapping("/test")
//    public String test(){
//        return assignService.test();
//    }

    /**
     * 安装负责人查看安装任务
     *
     * @param memberId
     * @return
     */
    @GetMapping("/tasks")
    public ResultData assigns(String memberId, Integer status, String search,String page,String pageLength,String reverse, String sortType) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装负责人的信息");
            return result;
        }
        if(!StringUtils.isEmpty(page)&&!StringUtils.isEmpty(pageLength)){
            result=assignService.fetchAssign(memberId, status, search,page,pageLength,reverse, sortType);
        }else {
            result = assignService.fetchAssign(memberId, status, search,reverse, sortType);
        }
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
    public ResultData overview(String memberId, Integer status, String search, String sortType) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(memberId)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供安装工人的信息");
            return result;
        }
        result = assignService.fetchOwnAssign(memberId, status, search, sortType);
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
    public ResultData submit(String assignId, String qrcode, String picture, Boolean wifi, String method, String description,String date) {
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
            result = assignService.submitAssign(assignId, qrcode, picture, wifi, method,date);
        } else {
            result = assignService.submitAssign(assignId, qrcode, picture, wifi, method,description,date);
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
        result = assignService.receive(assignId);
        return result;
    }

    /**
     * 由安装类型得到上传图片数量
     * @param assignType
     * @return
     */
    @GetMapping("/assignTypeInfo/one")
    public ResultData queryAssignTypeInfoByType(@RequestParam String assignType) {
        return assignService.queryAssignTypeInfoByType(assignType);
    }
    @PostMapping("/queryAssignState")
    public ResultData queryAssignState(@RequestParam String consumerPhone){
        return assignService.queryAssignState(consumerPhone);
    }
    @GetMapping("/assignTypeInfo/all")
    public ResultData queryAllAssignTypeInfo() {
        return assignService.queryAllAssignTypeInfo();
    }

    @PostMapping("/reservation/save")
    public ResultData saveReservations(@RequestBody Userassign userassign){
        return assignService.saveReservations(userassign);
    }
}


