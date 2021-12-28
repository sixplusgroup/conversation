package finley.gmair.controller;

import com.alibaba.excel.EasyExcel;
import finley.gmair.converter.OrderExcelConverter;
import finley.gmair.model.dto.ConsigneeDTO;
import finley.gmair.model.domain.UnifiedTrade;
import finley.gmair.model.dto.OrderExcel;
import finley.gmair.model.request.TradeQuery;
import finley.gmair.service.ConsigneeInfoUploadService;
import finley.gmair.service.TradeReadService;
import finley.gmair.service.TradeWriteService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2021-12-23 20:05
 * @description ：
 */

@CrossOrigin
@RestController
@RequestMapping("/order-centre/trade")
public class TradeController {

    private static final String FILE_ROOT_PATH = "/gmairOrderExcel/";

    private Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Resource
    ConsigneeInfoUploadService consigneeInfoUploadService;

    @Resource
    TradeWriteService tradeWriteService;

    @Resource
    TradeReadService tradeReadService;

    @Resource
    OrderExcelConverter orderExcelConverter;

    @GetMapping("/download")
    public void download(@RequestParam(required = false) String beginTime, @RequestParam(required = false) String endTime, HttpServletResponse response) throws IOException {
        Date beginDate = StringUtils.isEmpty(beginTime) ? null : TimeUtil.formatTimeToDate(beginTime);
        Date endDate = StringUtils.isEmpty(endTime) ? null : TimeUtil.formatTimeToDate(endTime);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("果麦订单汇总", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        TradeQuery tradeQuery = new TradeQuery();
        tradeQuery.setStartTime(beginDate);
        tradeQuery.setEndTime(endDate);
        List<UnifiedTrade> tradeList = tradeReadService.queryTradeList(tradeQuery);
        List<OrderExcel> orderExcelList = orderExcelConverter.toExcelList(tradeList);

        EasyExcel.write(response.getOutputStream(), OrderExcel.class)
                .sheet("订单汇总表").doWrite(orderExcelList);
    }

    @PostMapping("/consigneeInfo/upload")
    public ResultData upload(@RequestParam MultipartFile file,
                             @RequestParam(required = false) String password,
                             @RequestParam String shopId) {
        ResultData res = new ResultData();

        String filename = file.getOriginalFilename();
        // 以时间戳为文件父路径名，避免文件名冲突导致的文件覆盖
        String fileParentPath = FILE_ROOT_PATH + shopId + "/";
        // 保存Excel文件 -> 调用读取方法读取保存的文件 -> 用得到的数据更新数据库
        logger.info("upload consigneeInfo,shopId:{},filePath:{},filename:{},password:{}", shopId, fileParentPath, filename, password);
        try {
            boolean saveRes =
                    consigneeInfoUploadService.saveOrderPartInfoExcel(fileParentPath, filename, file.getBytes());
            if (saveRes) {
                String filePath = fileParentPath + filename;
                List<ConsigneeDTO> store = consigneeInfoUploadService.getConsigneeInfoList(filePath, password);
                tradeWriteService.defuzzyConsigneeInfoList(store, shopId);
            } else {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("save file " + filename + " failed!");
            }
        } catch (IOException e) {
            logger.error("file " + filename + " gets bytes failed" + e);
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("file gets bytes failed!");
            return res;
        } catch (Exception e) {
            logger.error("upload consigneeInfo excel failed", e);
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("upload consigneeInfo excel failed!");
            return res;
        }
        return res;
    }
}
