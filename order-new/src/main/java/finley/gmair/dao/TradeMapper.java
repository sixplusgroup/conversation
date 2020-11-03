package finley.gmair.dao;

import finley.gmair.model.dto.TbTradeDTO;
import finley.gmair.model.ordernew.Trade;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeMapper {
    int deleteByPrimaryKey(String tradeId);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(String tradeId);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);

    List<Trade> selectAll();

    /**
     * 根据tid查询返回对应的Trade
     */
    List<Trade> selectByTid(@Param("tid") Long tid);

    /**
     * 将淘宝方的单笔交易导入Trade表中
     */
    int insertSelectiveWithTradeDTO(TbTradeDTO tradeDTO);

    /**
     * 查询该tid在库中是否存在对应的交易
     */
    Integer countByTid(@Param("tid") Long tid);

    /**
     * 根据Trade主键更新交易状态
     */
    int updateStatusByTradeId(
            @Param("updatedStatus") String updatedStatus,
            @Param("tradeId") String tradeId);

    /**
     * 根据更新交易状态
     */
    int updateStatusByTid(
            @Param("updatedStatus") String updatedStatus,
            @Param("tid") Long tid);
}