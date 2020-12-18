package finley.gmair.service;

import com.taobao.api.domain.Trade;
import finley.gmair.model.dto.TbOrderPartInfo;
import finley.gmair.util.ResultData;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/17 15:41
 * @description ：
 */

public interface TbOrderService {
    /**
     * @param trade 淘宝交易类
     * @return ResultData
     * @description 全量/增量导入时处理单个Trade
     */
    ResultData handleTrade(Trade trade);

    /**
     * @param list
     * @return ResultData
     * @description Excel去模糊化时处理Trade集合
     */
    ResultData handlePartInfo(List<TbOrderPartInfo> list);
}