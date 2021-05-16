package finley.gmair.controller;

import finley.gmair.service.SkuItemService;
import finley.gmair.service.TbItemSyncService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ：tsl
 * @date ：Created in 2021/4/11 15:27
 * @description ：淘宝店铺商品同步与更新
 */

@CrossOrigin
@RestController
@RequestMapping("/order/skuItem")
public class SkuItemController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private TbItemSyncService tbItemSyncServiceImpl;

    @Autowired
    private SkuItemService skuItemServiceImpl;

    /**
     * 手动同步商品表
     * 添加不存在的商品项，更新已存在的商品项（根据num_iid和sku_iid）
     *
     * @return resultData
     */
    @PostMapping("/import")
    public ResultData manualImport() {
        ResultData res = tbItemSyncServiceImpl.fullImport();
        if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("skuItem import error:{}", res.getDescription());
            res.setDescription("同步请求淘宝API失败");
            return res;
        }
        return new ResultData();
    }

    /**
     * 获取所有商品项
     *
     * @return list
     */
    @GetMapping("/list")
    public ResultData list() {
        ResultData res = new ResultData();
        res.setData(skuItemServiceImpl.selectAll());
        return res;
    }

    /**
     * 根据itemId更新machineModel和fictitious
     *
     * @param itemId
     * @param machineModel
     * @param fictitious
     * @return resultData
     */
    @PostMapping("/update")
    public ResultData update(@RequestParam String itemId,
                             @RequestParam String machineModel,
                             @RequestParam boolean fictitious) {
        ResultData res = new ResultData();
        if (skuItemServiceImpl.selectByPrimaryKey(itemId) == null) {
            logger.error("cannot find skuItem by itemId:{}", itemId);
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("itemId不存在");
        }
        skuItemServiceImpl.updateMachineModelAndFictitious(itemId, machineModel, fictitious);
        return res;
    }
}
