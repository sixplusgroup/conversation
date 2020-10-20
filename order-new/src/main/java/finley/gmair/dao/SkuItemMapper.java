package finley.gmair.dao;


import finley.gmair.model.ordernew.SkuItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkuItemMapper {
    int deleteByPrimaryKey(String itemId);

    int insert(SkuItem record);

    int insertSelective(SkuItem record);

    SkuItem selectByPrimaryKey(String itemId);

    int updateByPrimaryKeySelective(SkuItem record);

    int updateByPrimaryKey(SkuItem record);

    List<SkuItem> selectAll();
}