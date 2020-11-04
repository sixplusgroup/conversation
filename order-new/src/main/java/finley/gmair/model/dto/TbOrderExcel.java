package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/2 14:28
 * @description ：
 */

@Data
public class TbOrderExcel {
    @ExcelProperty("渠道来源")
    private String tradeFrom;

    @ExcelProperty("订单号")
    private String tid;

    @ExcelProperty("机器型号")
    private String machineModel;

    @ExcelProperty("属性名称")
    private String propertyName;

    @ExcelProperty("数量")
    private Long num;

    @ExcelProperty("实收金额")
    private Double payment;

    @ExcelProperty("附加服务")
    private String additionalService;

    @ExcelProperty("付款状态")
    private String payStatus;

    @ExcelProperty("下单日期")
    private Date created;

    @ExcelProperty("订单状态")
    private String status;

    @ExcelProperty("用户姓名")
    private String receiverName;

    @ExcelProperty("联系方式")
    private String receiverMobile;

    @ExcelProperty("地区")
    private String receiverCity;

    @ExcelProperty("地址")
    private String receiverAddress;

    @ExcelProperty("发货时间")
    private Date deliveryTime;

    @ExcelProperty("发货计划")
    private String deliveryPlan;

    @ExcelProperty("风管发运")
    private String aidDutDelivery;

    @ExcelProperty("派单服务商")
    private String dispatchService;

    @ExcelProperty("派单计划")
    private String dispatchPlan;

    @ExcelProperty("安装时间")
    private Date installTime;
    
    @ExcelProperty("已安装台数")
    private Integer installNum;
}
