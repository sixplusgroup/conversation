package finley.gmair.converter;

import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.entity.UnifiedTradeDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-18 23:39
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface UnifiedTradeDataConverter {
    UnifiedTrade fromData(UnifiedTradeDO unifiedTradeDO);

    List<UnifiedTrade> fromDataList(List<UnifiedTradeDO> unifiedTradeDO);

    UnifiedTradeDO toData(UnifiedTrade unifiedTrade);

    List<UnifiedTradeDO> toDataList(List<UnifiedTrade> unifiedTrade);
}
