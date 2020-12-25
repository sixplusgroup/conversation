package finley.gmair.service;

import finley.gmair.model.dto.TbOrderExcel;
import finley.gmair.model.ordernew.Trade;

import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/2 14:13
 * @description ：
 */

public interface TradeService {
    List<Trade> selectAll();

    List<TbOrderExcel> selectTradeExcel(Date beginDate, Date endDate);
}
