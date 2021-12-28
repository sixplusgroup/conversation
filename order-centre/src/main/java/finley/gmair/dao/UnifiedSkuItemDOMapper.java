package finley.gmair.dao;

import finley.gmair.model.entity.UnifiedSkuItemDO;
import org.apache.ibatis.annotations.Param;

public interface UnifiedSkuItemDOMapper {
    int insert(UnifiedSkuItemDO record);

    int insertSelective(UnifiedSkuItemDO record);

    UnifiedSkuItemDO selectByPrimaryKey(String itemId);

    int updateByPrimaryKeySelective(UnifiedSkuItemDO record);

    int updateByPrimaryKey(UnifiedSkuItemDO record);

    UnifiedSkuItemDO selectByShopAndSku(@Param("shopId") String shopId, @Param("skuId") String skuId, @Param("numId") String numId);
}