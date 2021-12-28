package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:23
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JdConsigneeExcel {
    @ExcelProperty("订单号")
    private String tid;

    @ExcelProperty("联系电话")
    private String phone;

    @ExcelProperty("下单时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
