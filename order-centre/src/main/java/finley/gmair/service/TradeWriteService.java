package finley.gmair.service;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.dto.ConsigneeDTO;
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

    /**
     * 根据顾客信息和shopId去模糊化
     * @param list
     * @param shopId
     * @return 成功去模糊化记录条数
     */
    public int defuzzyConsigneeInfoList(List<ConsigneeDTO> list, String shopId) {
        int count = 0;
        if (CollectionUtils.isEmpty(list)) {
            return count;
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
            count += defuzzy(info, shopId) ? 1 : 0;
        }
        return count;
    }

    private boolean defuzzy(ConsigneeDTO info, String shopId) {
        boolean res = false;
        UnifiedTrade unifiedTrade = unifiedTradeRepo.findByTid(info.getTid(), shopId);
        if (unifiedTrade == null) {
            return res;
        }
        if (unifiedTrade.getIsFuzzy()) {
            unifiedTrade.setConsigneePhone(info.getPhone());
            unifiedTrade.setIsFuzzy(false);
            logger.info("defuzzy success,tid:{},shopId:{},consigneeInfo:{}", unifiedTrade.getTid(), JSON.toJSONString(info), shopId);
            unifiedTradeRepo.updateTradeOnly(unifiedTrade);
            res = true;
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
        return res;
    }
}
