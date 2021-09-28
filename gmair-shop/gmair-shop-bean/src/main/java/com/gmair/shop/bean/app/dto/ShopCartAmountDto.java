

package com.gmair.shop.bean.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购物车合计")
public class ShopCartAmountDto {

    @ApiModelProperty("总额")
    private Double totalMoney;

    @ApiModelProperty("总计")
    private Double finalMoney;

    @ApiModelProperty("减额")
    private Double subtractMoney;

    @ApiModelProperty("商品数量")
    private Integer count;

    @ApiModelProperty("积分总额")
    private Integer totalIntegral;

    @ApiModelProperty("是否需要现金")
    private Boolean isNeedCashOfAll;

    @ApiModelProperty("是否需要积分")
    private Boolean isNeedIntegralOfAll;
}
