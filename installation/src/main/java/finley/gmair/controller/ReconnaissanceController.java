package finley.gmair.controller;


import finley.gmair.form.installation.ReconnaissanceForm;
import finley.gmair.model.installation.Reconnaissance;
import finley.gmair.model.installation.ReconnaissanceStatus;
import finley.gmair.service.ReconnaissanceService;
import finley.gmair.util.DateFormatUtil;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.LocalDateParser;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/installation/reconnaissance")
public class ReconnaissanceController {

    @Autowired
    private ReconnaissanceService reconnaissanceService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResultData createReconnaissance(ReconnaissanceForm form) {
        ResultData result = new ResultData();
        Reconnaissance reconnaissance = new Reconnaissance(form.getOrderId(), form.getSetupMethod());
        if (!StringUtils.isEmpty(form.getDescription())) {
            reconnaissance.setDescription(form.getDescription());
        }
        reconnaissance.setStatus(ReconnaissanceStatus.TODO);

//        LocalDate reconDate = DateFormatUtil.convertToLocalDate(form.getReconDate());
//        if (reconDate == null) {
//            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
//            result.setDescription("勘测日期格式错误");
//            return result;
//        }
//        reconnaissance.setReconnaissanceDate(Timestamp.valueOf(LocalDateTime.of(reconDate, LocalTime.MIN)));


        ResultData response = reconnaissanceService.createReconnaissance(reconnaissance);
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试");
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }

        return result;
    }

    @RequestMapping(value = "/order/{orderId}", method = RequestMethod.GET)
    public ResultData orderReconnaissanceList(@PathVariable("orderId") String orderId) {
        ResultData result = new ResultData();
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("orderId", orderId);

        ResultData response = reconnaissanceService.fetchReconnaissance(condition);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试");
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(((List<Reconnaissance>)response.getData()).get(0));
        }

        return result;
    }

    @RequestMapping(value = "/{reconnaissanceId}/process", method = RequestMethod.POST)
    public ResultData reconnaissanceProcess(@PathVariable String reconnaissanceId, ReconnaissanceForm form) {
        ResultData result = new ResultData();
        Reconnaissance reconnaissance = new Reconnaissance(form.getOrderId(), form.getSetupMethod());
        reconnaissance.setReconId(reconnaissanceId);
        switch (form.getReconStatus()) {
            case 1: reconnaissance.setStatus(ReconnaissanceStatus.UNREACHABLE); break;
            case 2: reconnaissance.setStatus(ReconnaissanceStatus.FINISHED); break;
            default: reconnaissance.setStatus(ReconnaissanceStatus.TODO);
        }

        LocalDate reconDate = DateFormatUtil.convertToLocalDate(form.getReconDate());
        if (reconDate == null) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("勘测日期格式错误");
            return result;
        }
        reconnaissance.setReconnaissanceDate(Timestamp.valueOf(LocalDateTime.of(reconDate, LocalTime.MIN)));
        if (!StringUtils.isEmpty(form.getDescription())) {
            reconnaissance.setDescription(form.getDescription());
        }

        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        condition.put("reconId", reconnaissanceId);
        ResultData response = reconnaissanceService.fetchReconnaissance(condition);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(response.getResponseCode());
            result.setDescription("该预勘测编号不存在");
            return result;
        }

        response = reconnaissanceService.assignReconnaissance(reconnaissance);

        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setDescription("服务器忙，请稍后再试");
            result.setResponseCode(response.getResponseCode());
        } else if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
        } else {
            result.setData(response.getData());
        }

        return result;
    }

}
