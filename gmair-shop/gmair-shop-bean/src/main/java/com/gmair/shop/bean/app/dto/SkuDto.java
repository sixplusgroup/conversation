

package com.gmair.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gmair.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class SkuDto implements Serializable {

    private static final long serialVersionUID = 6457261945829470766L;

    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;
    @ApiModelProperty(value = "价格", required = true)
    private Double price;

    @ApiModelProperty(value = "库存(-1表示无穷)", required = true)
    private Integer stocks;

    @ApiModelProperty(value = "sku名称", required = true)
    private String skuName;

    @ApiModelProperty(value = "图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "销售属性组合字符串,格式是p1:v1;p2:v2", required = true)
    private String properties;

    /**
     * 是否需要现金
     */
    private Boolean isNeedCash = true;

    /**
     * 是否需要积分
     */
    private Boolean isNeedIntegral = false;

    /**
     * 积分价格
     */
    private Integer integralPrice;

}