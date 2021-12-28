package finley.gmair.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 13:51
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnifiedSkuItem {
    private String itemId;

    private Integer platform;

    private String shopId;

    private String numId;

    private String skuId;

    private String machineModel;

    private String channel;

    private Boolean isFictitious;

    private String title;

    private String propertiesName;

    private Double price;
}
