package com.gmair.shop.bean.app.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class IntegralDepositParam {
    @NotNull
    private Integer integral;
    @Size(max=80)
    private String description;
}
