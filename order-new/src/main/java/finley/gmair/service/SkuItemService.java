package finley.gmair.service;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/5 21:48
 * @description ：
 */


public interface SkuItemService {
    /**
     * 根据商品id和skuId查找商品型号
     *
     * @param numIid
     * @param skuId
     * @return
     */
    String selectMachineModelByNumIidAndSkuId(String numIid, String skuId);
}
