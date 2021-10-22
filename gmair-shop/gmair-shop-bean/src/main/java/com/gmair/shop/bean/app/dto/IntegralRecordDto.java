package com.gmair.shop.bean.app.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author Joby
 */
@Data
public class IntegralRecordDto {
    private Integer integralValue;
    private Boolean isAdd = false;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String description;
}
