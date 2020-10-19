package finley.gmair.model.ordernew;

import finley.gmair.model.Entity;
import lombok.Data;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/19 15:43
 * @description ：
 */

@Data
public class SkuItem extends Entity {
    private String item_id;
    private String num_iid;
    private String sku_id;
    private String title;
    private String properties_name;
    private String price;
}
