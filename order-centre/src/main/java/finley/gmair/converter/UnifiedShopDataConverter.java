package finley.gmair.converter;

import finley.gmair.model.domain.UnifiedShop;
import finley.gmair.model.entity.UnifiedShopDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 16:14
 * @description ：
 */

@Mapper(componentModel = "spring")
public interface UnifiedShopDataConverter {
    @Mapping(target = "shopAuthorizeInfo", source = ".")
    @Mapping(target = "shopPullInfo", source = ".")
    UnifiedShop fromData(UnifiedShopDO unifiedShopDO);

    @Mapping(target = "shopAuthorizeInfo", source = ".")
    @Mapping(target = "shopPullInfo", source = ".")
    List<UnifiedShop> fromData(List<UnifiedShopDO> unifiedShopDO);

    @Mapping(target = ".", source = "shopAuthorizeInfo")
    @Mapping(target = ".", source = "shopPullInfo")
    UnifiedShopDO toData(UnifiedShop shop);
}
