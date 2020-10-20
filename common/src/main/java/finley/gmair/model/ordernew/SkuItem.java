package finley.gmair.model.ordernew;

import finley.gmair.model.Entity;
import lombok.Data;

import java.util.Date;

/**
 * @author ：tsl
 * @date ：Created in 2020/10/19 15:43
 * @description ：商品项表
 */

@Data
public class SkuItem extends Entity{
    /**
     * 主键
     */
    private String itemId;

    /**
     * 商品id
     */
    private String numIid;

    /**
     * 商品的最小库存单位Sku的id
     */
    private String skuId;

    /**
     * 商品标题,不能超过60字节
     */
    private String title;

    /**
     * 属性名称
     */
    private String propertiesName;

    /**
     * 属于这个sku的商品的价格 取值范围:0-100000000;精确到2位小数;单位:元
     */
    private Double price;

    private Boolean blockFlag;

    private Date createAt;
}
