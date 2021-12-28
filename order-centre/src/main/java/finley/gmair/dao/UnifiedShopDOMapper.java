package finley.gmair.dao;

import finley.gmair.model.entity.UnifiedShopDO;

import java.util.List;

public interface UnifiedShopDOMapper {
    int insert(UnifiedShopDO record);

    int insertSelective(UnifiedShopDO record);

    UnifiedShopDO selectByPrimaryKey(String shopId);

    int updateByPrimaryKeySelective(UnifiedShopDO record);

    int updateByPrimaryKey(UnifiedShopDO record);

    List<UnifiedShopDO> selectAll();

    void updatePullInfo(UnifiedShopDO record);
}