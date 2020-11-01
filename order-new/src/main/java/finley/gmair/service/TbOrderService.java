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
     * @param trade from com.taobao.api.domain.Trade
     * @return ResultData
     */
    ResultData handleTrade(Trade trade);
}