package finley.gmair.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.form.drift.DataItemForm;
import finley.gmair.form.drift.ReportForm;
import finley.gmair.form.drift.TemplateForm;
import finley.gmair.model.drift.DataItem;
import finley.gmair.model.drift.DriftOrder;
import finley.gmair.model.drift.DriftReport;
import finley.gmair.model.drift.ReportTemplate;
import finley.gmair.service.DriftReportService;
import finley.gmair.service.OrderService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drift/report")
public class ReportController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private DriftReportService driftReportService;

    @Autowired
    private OrderService orderService;

    /**
     * 创建报告模板
     * @param templateForm
     * @return
     */
    @PostMapping("/template/create")
    public ResultData createTemplate(TemplateForm templateForm){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(templateForm.getDetectName())||StringUtils.isEmpty(templateForm.getEvaluateBasis())||StringUtils.isEmpty(templateForm.getKnowledge())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供完整的条件");
            return result;
        }
        ReportTemplate reportTemplate = new ReportTemplate(templateForm.getDetectName(),templateForm.getEvaluateBasis(),templateForm.getKnowledge());
        result = driftReportService.createReportTemplate(reportTemplate);
        logger.info(JSONObject.toJSONString(reportTemplate));
        return result;
    }

    /**
     * 提交单点数据
     * @param form
     * @return
     */
    @PostMapping("/data/create")
    public ResultData createReportData(DataItemForm form){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(form.getReportId())||StringUtils.isEmpty(form.getPosition())||StringUtils.isEmpty(form.getArea())||StringUtils.isEmpty(form.getData())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供完整的条件");
            return result;
        }
        DataItem dataItem = new DataItem(form.getReportId(),form.getPosition(),form.getArea(),form.getData());
        result = driftReportService.createDataItem(dataItem);
        logger.info(JSONObject.toJSONString(dataItem));
        return result;
    }

    /**
     * 创建报告
     * @param form
     * @return
     */
    @PostMapping("/create")
    public ResultData createReport(ReportForm form){
        ResultData result = new ResultData();
        System.out.println(JSONObject.toJSONString(form));
        if(StringUtils.isEmpty(form.getOrderId())||StringUtils.isEmpty(form.getDetectDate())||StringUtils.isEmpty(form.getIsClosed())){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供完整的表单");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("orderId",form.getOrderId());
        condition.put("blockFlag",false);
        ResultData response = orderService.fetchDriftOrder(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        boolean closed = !form.getIsClosed().equals("0");
        DriftOrder order = ((List<DriftOrder>) response.getData()).get(0);
        DriftReport driftReport = new DriftReport(order.getConsumerId(),order.getOrderId(),order.getConsignee(),order.getPhone(),order.getAddress(), TimeUtil.formatTimeToDate(form.getDetectDate()),closed,form.getReportTemplateId());
        if(!StringUtils.isEmpty(form.getLiveDate())){
            driftReport.setLiveDate(TimeUtil.formatTimeToDate(form.getLiveDate()));
        }
        if(!StringUtils.isEmpty(form.getDecorateDate())){
            driftReport.setDecorateDate(TimeUtil.formatTimeToDate(form.getDecorateDate()));
        }
        if(!StringUtils.isEmpty(form.getTemperature())){
            driftReport.setTemperature(form.getTemperature());
        }
        response = driftReportService.createDriftReport(driftReport);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription("创建drift report失败");
            return result;
        }
        result.setData(response.getData());
        String reportId = ((DriftReport) response.getData()).getReportId();

        //处理数据项
        if(!StringUtils.isEmpty(form.getDataItem())){
            JSONArray dataItems = JSONArray.parseArray(form.getDataItem());
            for (Object e : dataItems) {
                DataItemForm dataItemForm = new DataItemForm();
                dataItemForm.setReportId(reportId);
                JSONObject jsonObject = JSONObject.parseObject(e.toString());
                dataItemForm.setArea(jsonObject.getDouble("area"));
                dataItemForm.setPosition(jsonObject.getString("position"));
                dataItemForm.setData(jsonObject.getDouble("data"));
                response = createReportData(dataItemForm);
                if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
                    result.setResponseCode(response.getResponseCode());
                    result.setDescription(response.getDescription());
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * 根据orderId查询drift report
     * @param orderId
     * @return
     */
    @GetMapping("/query")
    ResultData getDriftReport(String orderId){
        ResultData result = new ResultData();
        if(StringUtils.isEmpty(orderId)){
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("请提供orderId");
            return result;
        }
        Map<String,Object> condition = new HashMap<>();
        condition.put("orderId",orderId);
        condition.put("blockFlag",false);
        ResultData response = driftReportService.fetchDriftReport(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }

    /**
     * 根据reportTemplateId获取报告模板信息
     * @param reportTemplateId
     * @return
     */
    @GetMapping("/template/query")
    ResultData getTemplate(String reportTemplateId){
        ResultData result = new ResultData();
        Map<String,Object> condition = new HashMap<>();
        condition.put("blockFlag",false);
        condition.put("reportTemplateId",reportTemplateId);
        ResultData response = driftReportService.fetchReportTemplate(condition);
        if(response.getResponseCode()!=ResponseCode.RESPONSE_OK){
            result.setResponseCode(response.getResponseCode());
            result.setDescription(response.getDescription());
            return result;
        }
        result.setData(response.getData());
        return result;
    }
}
