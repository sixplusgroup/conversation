package finley.gmair.dao;

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
}