package finley.gmair.repo;

import com.alibaba.fastjson.JSON;
import finley.gmair.converter.UnifiedSkuItemDataConverter;
import finley.gmair.dao.UnifiedSkuItemDOMapper;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.entity.UnifiedSkuItemDO;
import finley.gmair.util.IDGenerator;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 0:18
 * @description ：
 */

@Repository
public class UnifiedSkuItemRepo {
    @Resource
    UnifiedSkuItemDataConverter unifiedSkuItemDataConverter;

    @Resource
    UnifiedSkuItemDOMapper unifiedSkuItemDOMapper;

    public void save(UnifiedSkuItem skuItem) {
        System.out.println(JSON.toJSONString(skuItem));
        if (skuItem.getItemId() != null) {
            // 更新
            UnifiedSkuItemDO unifiedSkuItemDO = unifiedSkuItemDataConverter.toData(skuItem);
            int updateResult = unifiedSkuItemDOMapper.updateByPrimaryKeySelective(unifiedSkuItemDO);
        } else {
            // 插入
            skuItem.setItemId(IDGenerator.generate("ITE"));
            UnifiedSkuItemDO unifiedSkuItemDO = unifiedSkuItemDataConverter.toData(skuItem);
            int insertResult = unifiedSkuItemDOMapper.insertSelective(unifiedSkuItemDO);
        }
    }

    /**
     * 根据shopId,skuItemId,numId查询,如果存在填充itemId
     * 填充channel,machine_model,is_fictitious
     */
    public void fillPrimaryKey(UnifiedSkuItem skuItem) {
        UnifiedSkuItem skuItemInDb = findByShopAndSku(skuItem.getShopId(), skuItem.getSkuId(), skuItem.getNumId());

        if (null != skuItemInDb) {
            skuItem.setItemId(skuItemInDb.getItemId());
            skuItem.setChannel(skuItemInDb.getChannel());
            skuItem.setMachineModel(skuItemInDb.getMachineModel());
            skuItem.setIsFictitious(skuItemInDb.getIsFictitious());
        }
    }

    public UnifiedSkuItem findByShopAndSku(String shopId, String skuId, String numId) {
        UnifiedSkuItemDO skuItemDO = unifiedSkuItemDOMapper.selectByShopAndSku(shopId, skuId, numId);
        return unifiedSkuItemDataConverter.fromData(skuItemDO);
    }

    public UnifiedSkuItem findByPrimaryKey(String itemId) {
        UnifiedSkuItemDO skuItemDO = unifiedSkuItemDOMapper.selectByPrimaryKey(itemId);
        return unifiedSkuItemDataConverter.fromData(skuItemDO);
    }

}
