package finley.gmair.converter;

import finley.gmair.model.domain.UnifiedOrder;
import finley.gmair.model.entity.UnifiedOrderDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-19 15:11
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface UnifiedOrderDataConverter {
    UnifiedOrder fromData(UnifiedOrderDO unifiedOrderDO);

    List<UnifiedOrder> fromDataList(List<UnifiedOrderDO> unifiedOrderDOList);

    UnifiedOrderDO toData(UnifiedOrder unifiedOrder);

    List<UnifiedOrderDO> toDataList(List<UnifiedOrder> unifiedOrderList);
}
