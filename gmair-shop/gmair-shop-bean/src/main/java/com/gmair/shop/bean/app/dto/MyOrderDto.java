

package com.gmair.shop.bean.app.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("我的订单")
public class MyOrderDto {
	
	@ApiModelProperty(value = "订单项",required=true)
	private List<MyOrderItemDto> orderItemDtos;
	
	@ApiModelProperty(value = "订单号",required=true)
	private String orderNumber;
	
	@ApiModelProperty(value = "总价",required=true)
	private Double actualTotal;
	
	@ApiModelProperty(value = "订单状态",required=true)
	private Integer status;

	@ApiModelProperty(value = "积分总额",required=true)
	private Integer totalIntegral;

	@ApiModelProperty(value = "是否需要现金",required=true)
	private Boolean isNeedCashOfAll;

	@ApiModelProperty(value = "是否需要积分",required=true)
	private Boolean isNeedIntegralOfAll;
	
}
