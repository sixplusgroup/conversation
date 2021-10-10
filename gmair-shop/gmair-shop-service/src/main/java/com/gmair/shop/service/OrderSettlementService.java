

package com.gmair.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gmair.shop.bean.model.OrderSettlement;

/**
 *
 *
 */
public interface OrderSettlementService extends IService<OrderSettlement> {

	/**
	 * 根据内部订单号更新order settlement
	 */
	void updateSettlementsByPayNo(String outTradeNo, String transactionId);
}
