package finley.gmair.converter;

import com.jd.open.api.sdk.domain.ware.SkuReadService.response.searchSkuList.Sku;
import finley.gmair.model.domain.UnifiedSkuItem;
import finley.gmair.model.enums.PlatformEnum;
import org.springframework.stereotype.Component;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 14:28
 * @description ：
 */

@Component
public class JdSkuItemConverter {
    public UnifiedSkuItem jdSkuItem2UnifiedSkuItem(Sku originalSku) {
        UnifiedSkuItem unifiedSkuItem = new UnifiedSkuItem();
        unifiedSkuItem.setPlatform(PlatformEnum.JD.getValue());
        unifiedSkuItem.setNumId(String.valueOf(originalSku.getWareId()));
        unifiedSkuItem.setSkuId(String.valueOf(originalSku.getSkuId()));
        unifiedSkuItem.setTitle(String.valueOf(originalSku.getWareTitle()));
        unifiedSkuItem.setPrice(originalSku.getJdPrice().doubleValue());
        unifiedSkuItem.setPropertiesName(originalSku.getSkuName());
        return unifiedSkuItem;
    }
}
