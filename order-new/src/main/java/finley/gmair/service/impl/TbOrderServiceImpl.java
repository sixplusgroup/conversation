package finley.gmair.service.impl;

import com.taobao.api.domain.Trade;
import finley.gmair.service.TbOrderService;
import org.springframework.stereotype.Service;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:46
 * @description ：
 */

@Service
public class TbOrderServiceImpl implements TbOrderService {
    @Override
    public void handleTrade(Trade trade) {
        System.out.println(trade.toString());
    }
}
