package finley.gmair.dao;

import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.model.ordernew.Trade;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeMapper {
    int deleteByPrimaryKey(String tradeId);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(String tradeId);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    /**
     * 将淘宝方的单笔交易导入Trade表中
     */
    int insertSelectiveWithTradeDTO(TbTradeDTO tradeDTO);
}