package finley.gmair.controller;

import com.alibaba.fastjson.JSON;
import finley.gmair.model.request.PullRequestDTO;
import finley.gmair.service.TradePullService;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ：tsl
 * @date ：Created in 2020/12/20 20:45
 * @description ：全平台订单拉取统一接口
 */

@CrossOrigin
@RestController
@RequestMapping("/order-centre/trade")
public class TradePullController {

    @Resource
    TradePullService tradePullService;

    private Logger logger = LoggerFactory.getLogger(TradePullController.class);

    @PostMapping("/singlePull")
    public ResultData singlePull(@RequestParam String shopId, @RequestParam String tid) {
        try {
            tradePullService.singlePull(shopId, tid);
        } catch (Exception e) {
            logger.error("singlePull error,shopId:{},tid:{}", shopId, tid, e);
            return ResultData.error(e.getMessage());
        }
        return new ResultData();
    }

    @PostMapping("/batchPull")
    public ResultData batchPull(@RequestParam String shopId, @RequestBody PullRequestDTO requestDTO) {
        try {
            tradePullService.batchPull(shopId, requestDTO);
        } catch (Exception e) {
            logger.error("batchPull error,shopId:{},requestDTO:{}", shopId, JSON.toJSONString(requestDTO), e);
            return ResultData.error(e.getMessage());
        }
        return new ResultData();
    }


    @PostMapping("/schedulePull")
    public ResultData schedulePull(@RequestParam String shopId) {
        try {
            tradePullService.schedulePull(shopId);
        } catch (Exception e) {
            logger.error("batchPull error", e);
            return ResultData.error(e.getMessage());
        }
        return new ResultData();
    }

    @PostMapping("/schedulePullAll")
    public ResultData schedulePullAll() {
        try {
            tradePullService.schedulePullAll();
        } catch (Exception e) {
            logger.error("batchPull error", e);
            return ResultData.error(e.getMessage());
        }
        return new ResultData();
    }
}
