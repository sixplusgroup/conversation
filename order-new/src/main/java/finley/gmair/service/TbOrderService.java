package finley.gmair.service;

import com.taobao.api.domain.Trade;
import finley.gmair.util.ResultData;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:41
 * @description ：
 */

public interface TbOrderService {
    /**
     * 处理订单
     * @param trade
     */
    ResultData handleTrade(Trade trade);
}
