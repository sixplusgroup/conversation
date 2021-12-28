package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 23:31
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderExcel {

    @ExcelProperty("平台")
    private String platform;

    @ExcelProperty("渠道来源")
    private String channel;

    @ExcelProperty("订单号")
    private String tid;

    @ExcelProperty("机器型号")
    private String machineModel;

    @ExcelProperty("属性名称")
    private String propertiesName;

    @ExcelProperty("数量")
    private String num;

    @ExcelProperty("实收金额")
    private String payment;

    @ExcelProperty("下单日期")
    private String payTime;

    @ExcelProperty("订单状态")
    private String status;

    @ExcelProperty("用户姓名")
    private String consigneeName;

    @ExcelProperty("联系方式")
    private String consigneePhone;

    @ExcelProperty("地区")
    private String consigneeCity;

    @ExcelProperty("地址")
    private String consigneeAddress;
}
