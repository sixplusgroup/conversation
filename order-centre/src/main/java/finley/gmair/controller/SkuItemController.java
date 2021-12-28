package finley.gmair.controller;

import com.alibaba.excel.EasyExcel;
import finley.gmair.converter.SkuItemExcelConverter;
import finley.gmair.listener.SkuItemExcelUploadListener;
import finley.gmair.model.dto.SkuItemExcel;
import finley.gmair.service.SkuItemPullService;
import finley.gmair.service.SkuItemWriteService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-24 0:04
 * @description ：
 */

@CrossOrigin
@RestController
@RequestMapping("/order-centre/skuItem")
public class SkuItemController {
    @Resource
    SkuItemPullService skuItemPullService;

    @Resource
    SkuItemWriteService skuItemWriteService;

    @Resource
    SkuItemExcelConverter skuItemExcelConverter;

    private Logger logger = LoggerFactory.getLogger(SkuItemController.class);

    @PostMapping("/pull")
    public ResultData pull(@RequestParam String shopId) {
        try {
            skuItemPullService.pull(shopId);
        } catch (Exception e) {
            logger.error("pull error,shopId:{}", shopId, e);
            return ResultData.error(e.getMessage());
        }
        return new ResultData();
    }

    @PostMapping("/upload")
    public ResultData upload(@RequestParam MultipartFile file,
                             @RequestParam String shopId) {
        logger.info("upload skuItemInfo,shopId:{}", shopId);
        try {
            EasyExcel.read(file.getInputStream(), SkuItemExcel.class,
                    new SkuItemExcelUploadListener(skuItemWriteService, skuItemExcelConverter)).sheet().doRead();
        } catch (IOException e) {
            logger.error("file " + file.getOriginalFilename() + " gets bytes failed" + e);
            return ResultData.error("file gets bytes failed!");
        }
        return new ResultData();
    }
}
