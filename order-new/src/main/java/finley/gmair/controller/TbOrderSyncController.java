package finley.gmair.controller;

import finley.gmair.pool.OrderNewPool;
import finley.gmair.service.TbOrderSyncService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: Bright Chan
 * @date: 2020/10/27 9:22
 * @description: TbOrderSyncController
 */

@RestController
@RequestMapping("/order/tbOrderSync")
public class TbOrderSyncController {

    private Logger logger = LoggerFactory.getLogger(TbOrderSyncController.class);

    @Autowired
    private TbOrderSyncService tbOrderSyncService;

    @PostMapping("/import/incremental")
    public ResultData incrementalImport() {
        OrderNewPool.getOrderNewPool().execute(() -> {
            ResultData res = tbOrderSyncService.incrementalImport();
            if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("hourly check: incrementalImport. failed!");
            }
        });

        return new ResultData();
    }

    @PostMapping("/import/manualIncremental")
    public ResultData manualIncrementalImport() {
        ResultData res = tbOrderSyncService.incrementalImport();
        if (res.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("manualIncrementalImport. failed!");
        }
        return res;
    }

    @PostMapping("/import/full")
    public ResultData fullImport() {
        return tbOrderSyncService.fullImport();
    }

    @PostMapping("/import/manualByCreated")
    public ResultData manualImportByCreated(@RequestParam Date startCreated, @RequestParam Date endCreated) {
        return tbOrderSyncService.manualImportByCreated(startCreated, endCreated);
    }

    @PostMapping("/import/manualByModified")
    public ResultData manualImportByModified(@RequestParam Date startModified, @RequestParam Date endModified,
                                             @RequestParam(required = false) Date startCreated) {
        return tbOrderSyncService.manualImportByModified(startModified, endModified, startCreated);
    }

    @PostMapping("/import/manualByTid")
    public ResultData manualImportByTid(@RequestParam Long tid) {
        return tbOrderSyncService.manualImportByTid(tid);
    }
}
