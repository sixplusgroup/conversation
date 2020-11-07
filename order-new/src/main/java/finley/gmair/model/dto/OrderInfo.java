package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/5 20:57
 * @description ：order表+trade表查询信息
 */

@Data
public class OrderInfo {
    private Long tid;

    private Long numIid;

    private Long skuId;

    private String skuPropertiesName;

    private Long num;

    private Double payment;

    private Date created;

    private Date payTime;

    private String status;

    private String receiverName;

    private String receiverMobile;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;
}
