package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.dto.ConsigneeDTO;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.repo.UnifiedTradeRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:43
 * @description ：
 */

@Service
public class TradeWriteService {

    private Logger logger = LoggerFactory.getLogger(TradeWriteService.class);

    @Resource
    UnifiedTradeRepo unifiedTradeRepo;

    @Resource
    CrmPushService crmPushService;

    @Resource
    DriftPushService driftPushService;

    public void defuzzyConsigneeInfoList(List<ConsigneeDTO> list, String shopId) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //先按下单时间排序保证顺序处理
        list.sort((o1, o2) -> {
            if (o1.getCreateTime() == null || o2.getCreateTime() == null) {
                return 0;
            }
            if (o1.getCreateTime().after(o2.getCreateTime())) {
                return 1;
            } else if (o1.getCreateTime().before(o2.getCreateTime())) {
                return -1;
            }
            return 0;
        });
        for (ConsigneeDTO info : list) {
            try {
                defuzzy(info, shopId);
            } catch (Exception e) {
                logger.error("defuzzy error,consigneeInfo:{}", JSON.toJSONString(info), e);
            }
        }
    }

    private void defuzzy(ConsigneeDTO info, String shopId) {
        UnifiedTrade unifiedTrade = unifiedTradeRepo.findByTid(info.getTid(), shopId);
        if (unifiedTrade == null) {
            return;
        }
        if (unifiedTrade.getIsFuzzy()) {
            unifiedTrade.setConsigneePhone(info.getPhone());
            unifiedTrade.setIsFuzzy(false);
            logger.info("defuzzy success,tid:{},shopId:{},consigneeInfo:{}", unifiedTrade.getTid(), JSON.toJSONString(info), shopId);
            unifiedTradeRepo.updateTradeOnly(unifiedTrade);
        }
        try {
            driftPushService.pushCreate(unifiedTrade.getTradeId());
        } catch (Exception e) {
            logger.error("exception occur when pushCreate to drift", e);
        }
        try {
            crmPushService.pushCreate(unifiedTrade.getTradeId());
        } catch (Exception e) {
            logger.error("exception occur when pushCreate to crm", e);
        }
    }
}
