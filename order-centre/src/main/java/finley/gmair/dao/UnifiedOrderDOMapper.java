package finley.gmair.dao;

import finley.gmair.model.entity.UnifiedOrderDO;

import java.util.List;

public interface UnifiedOrderDOMapper {
    int insert(UnifiedOrderDO record);

    int insertSelective(UnifiedOrderDO record);

    UnifiedOrderDO selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(UnifiedOrderDO record);

    int updateByPrimaryKey(UnifiedOrderDO record);

    List<UnifiedOrderDO> selectByTradeId(String tradeId);
}