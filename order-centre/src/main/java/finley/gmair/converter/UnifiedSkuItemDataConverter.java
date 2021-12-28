package finley.gmair.converter;

import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.entity.UnifiedSkuItemDO;
import org.mapstruct.Mapper;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 14:02
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface UnifiedSkuItemDataConverter {
    UnifiedSkuItem fromData(UnifiedSkuItemDO unifiedSkuItemDO);

    UnifiedSkuItemDO toData(UnifiedSkuItem unifiedSkuItem);
}
