package finley.gmair.dao;


import finley.gmair.model.ordernew.SkuItem;

public interface SkuItemMapper {
    int deleteByPrimaryKey(String itemId);

    int insert(SkuItem record);

    int insertSelective(SkuItem record);

    SkuItem selectByPrimaryKey(String itemId);

    int updateByPrimaryKeySelective(SkuItem record);

    int updateByPrimaryKey(SkuItem record);
}