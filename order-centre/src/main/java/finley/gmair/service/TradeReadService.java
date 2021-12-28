package finley.gmair.service;

import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.request.TradeQuery;
import finley.gmair.repo.UnifiedTradeRepo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-25 23:29
 * @description ：
 */

@Service
public class TradeReadService {
    @Resource
    UnifiedTradeRepo tradeRepo;

    public List<UnifiedTrade> queryTradeList(TradeQuery query) {
        return tradeRepo.query(query);
    }
}
