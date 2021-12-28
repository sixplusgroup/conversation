package finley.gmair.model.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-28 13:45
 * @description ：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkuItemExcel {

    @ExcelProperty("item_id")
    private String itemId;

    @ExcelProperty("shop_id")
    private String shopId;

    @ExcelProperty("num_id")
    private String numId;

    @ExcelProperty("sku_id")
    private String skuId;

    @ExcelProperty("machine_model")
    private String machineModel;

    @ExcelProperty("is_fictitious")
    private Boolean isFictitious;
}
