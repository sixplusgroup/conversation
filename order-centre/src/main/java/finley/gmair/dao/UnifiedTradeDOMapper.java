package finley.gmair.dao;

import finley.gmair.model.entity.UnifiedTradeDO;
import finley.gmair.model.request.TradeQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UnifiedTradeDOMapper {
    int insert(UnifiedTradeDO record);

    int insertSelective(UnifiedTradeDO record);

    UnifiedTradeDO selectByPrimaryKey(String tradeId);

    int updateByPrimaryKeySelective(UnifiedTradeDO record);

    int updateByPrimaryKey(UnifiedTradeDO record);

    UnifiedTradeDO selectByTidAndPlatform(@Param("tid") String tid, @Param("platform") int platform);

    UnifiedTradeDO selectByTidAndShopId(@Param("tid") String tid, @Param("shopId") String shopId);

    List<UnifiedTradeDO> query(TradeQuery query);
}