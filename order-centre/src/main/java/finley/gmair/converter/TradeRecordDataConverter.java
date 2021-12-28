package finley.gmair.converter;

import finley.gmair.model.domain.TradeRecord;
import finley.gmair.model.entity.TradeRecordDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 16:00
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface TradeRecordDataConverter {
    @Mapping(target = "recordTime", source = "sysCreateTime")
    TradeRecord fromData(TradeRecordDO tradeRecordDO);

    TradeRecordDO toData(TradeRecord tradeRecord);
}
