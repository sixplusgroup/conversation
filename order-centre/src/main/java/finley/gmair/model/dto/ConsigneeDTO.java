package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:20
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsigneeDTO {
    private String tid;

    private String phone;

    private Date createTime;
}
