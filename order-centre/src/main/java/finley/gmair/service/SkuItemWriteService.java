package finley.gmair.service;

import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.dto.SkuItemDTO;
import finley.gmair.repo.UnifiedSkuItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-28 13:57
 * @description ：
 */

@Service
public class SkuItemWriteService {
    @Resource
    UnifiedSkuItemRepo skuItemRepo;

    public void updateSkuItem(List<SkuItemDTO> skuItemDTOList) {
        if (CollectionUtils.isEmpty(skuItemDTOList)) {
            return;
        }
        skuItemDTOList.forEach(this::updateSkuItem);
    }

    public void updateSkuItem(SkuItemDTO skuItemDTO) {
        UnifiedSkuItem skuItem;
        if (skuItemDTO.getItemId() != null) {
            skuItem = skuItemRepo.findByPrimaryKey(skuItemDTO.getItemId());
        } else {
            skuItem = skuItemRepo.findByShopAndSku(skuItemDTO.getShopId(), skuItemDTO.getNumId(), skuItemDTO.getNumId());
        }
        if (skuItem == null) {
            return;
        }
        skuItem.setIsFictitious(skuItemDTO.getIsFictitious());
        skuItem.setMachineModel(skuItemDTO.getMachineModel());
        skuItemRepo.save(skuItem);
    }
}
