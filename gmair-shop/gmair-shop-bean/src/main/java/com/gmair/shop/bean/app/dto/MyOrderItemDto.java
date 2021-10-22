

package com.gmair.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gmair.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("我的订单-订单项")
@Data
public class MyOrderItemDto {

    @ApiModelProperty(value = "商品图片", required = true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "商品名称", required = true)
    private String prodName;

    @ApiModelProperty(value = "商品数量", required = true)
    private Integer prodCount;

    @ApiModelProperty(value = "商品价格", required = true)
    private Double price;

    @ApiModelProperty(value = "是否需要现金", required = true)
    private Boolean isNeedCash = true;

    @ApiModelProperty(value = "是否需要积分", required = true)
    private Boolean isNeedIntegral = false;

    @ApiModelProperty(value = "积分单价", required = true)
    private Integer integralPrice;

    @ApiModelProperty(value = "积分总额", required = true)
    private Integer integralTotalAmount;

    @ApiModelProperty(value = "skuName", required = true)
    private String skuName;

}
