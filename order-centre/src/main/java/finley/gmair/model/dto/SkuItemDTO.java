package finley.gmair.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-28 14:06
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemDTO {

    private String itemId;

    private String shopId;

    private String numId;

    private String skuId;

    private String machineModel;

    private Boolean isFictitious;
}
