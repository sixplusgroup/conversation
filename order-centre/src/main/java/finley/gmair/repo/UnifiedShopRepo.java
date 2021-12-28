package finley.gmair.repo;

import finley.gmair.converter.UnifiedShopDataConverter;
import finley.gmair.dao.UnifiedShopDOMapper;
import finley.gmair.model.domain.UnifiedShop;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-21 15:51
 * @description ：
 */

@Repository
public class UnifiedShopRepo {
    @Resource
    UnifiedShopDOMapper unifiedShopDOMapper;

    @Resource
    UnifiedShopDataConverter unifiedShopDataConverter;

    public UnifiedShop findById(String id) {
        return unifiedShopDataConverter.fromData(unifiedShopDOMapper.selectByPrimaryKey(id));
    }

    public List<UnifiedShop> findAll() {
        return unifiedShopDataConverter.fromData(unifiedShopDOMapper.selectAll());
    }

    public void updatePullInfo(UnifiedShop shop) {
        unifiedShopDOMapper.updatePullInfo(unifiedShopDataConverter.toData(shop));
    }
}
