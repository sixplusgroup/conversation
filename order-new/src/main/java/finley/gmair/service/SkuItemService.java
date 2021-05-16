package finley.gmair.service;

import finley.gmair.model.ordernew.SkuItem;

import java.util.List;

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
     * @return 商品项型号
     */
    String selectMachineModelByNumIidAndSkuId(String numIid, String skuId);

    /**
     * 查询所有商品项
     *
     * @return 商品项列表
     */
    List<SkuItem> selectAll();

    /**
     * 根据itemId更新商品项型号和是否虚拟
     *
     * @param itemId
     * @param machineModel
     * @param fictitious
     */
    void updateMachineModelAndFictitious(String itemId, String machineModel, boolean fictitious);

    /**
     * 根据主键itemId查询SkuItem
     *
     * @param itemId
     * @return SkuItem
     */
    SkuItem selectByPrimaryKey(String itemId);
}
