package com.gmair.shop.bean.app.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author Joby
 */
@Data
public class PSupplementaryIntegralParam {
    @NotNull
    private String deviceModel;
    @Size(max=80)
    private String description;
    @NotNull
    private String pictures;
}
