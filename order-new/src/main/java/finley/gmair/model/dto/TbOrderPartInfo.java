package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/11/3 14:07
 * @description: TbOrderPartInfo，从Excel表中读取所需的部分数据字段组成的对象
 */

@Data
public class TbOrderPartInfo {

    @ExcelProperty("订单编号")
    private String orderId;

    @ExcelProperty("收货人姓名")
    private String receiver;

    @ExcelProperty("收货地址")
    private String deliveryAddress;

    @ExcelProperty("联系手机")
    private String phone;

    @ExcelProperty("订单创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
