package finley.gmair.controller;

import com.alibaba.excel.EasyExcel;
import finley.gmair.model.dto.TbOrderExcel;
import finley.gmair.model.dto.TbOrderPartInfo;
import finley.gmair.service.TbOrderPartInfoService;
import finley.gmair.service.TbOrderService;
import finley.gmair.service.TradeService;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/2 13:57
 * @description ：
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    private static final String XLSX = ".xlsx", XLS = ".xls", FILE_ROOT_PATH = "/gmairTbOrderExcel/";

    @Autowired
    private TradeService tradeServiceImpl;

    @Autowired
    private TbOrderPartInfoService tbOrderPartInfoService;

    @Autowired
    private TbOrderService tbOrderService;

    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode("果麦订单汇总", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TbOrderExcel.class)
                .sheet("订单汇总表").doWrite(tradeServiceImpl.selectAllTradeExcel());
    }

    /**
     * 上传Excel表格，读取数据并更新数据库
     * @return 上传和更新结果，成功与否
     */
    @PostMapping("/uploadAndSync")
    public ResultData uploadAndSync(@RequestParam MultipartFile file, @RequestParam String password) {
        ResultData res = new ResultData();

        // 参数检查
        if (file == null || StringUtils.isEmpty(password)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("missing parameter(s)");
            return res;
        }

        String filename = file.getOriginalFilename();
        // 文件名后缀检查
        if (!filename.endsWith(XLS) && !filename.endsWith(XLSX)) {
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("error file type!");
            return res;
        }

        // 以时间戳为文件父路径名，避免文件名冲突导致的文件覆盖
        String fileParentPath = FILE_ROOT_PATH + new Date().getTime() + "/";

        // 保存Excel文件 -> 调用读取方法读取保存的文件 -> 用得到的数据更新数据库
        try {
            boolean saveRes =
                    tbOrderPartInfoService.saveOrderPartInfoExcel(fileParentPath, filename, file.getBytes());
            if (saveRes) {
                String filePath = fileParentPath + filename;
                ResultData response = tbOrderPartInfoService.getTbOrderPartInfo(filePath, password);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    List<TbOrderPartInfo> store = (List<TbOrderPartInfo>) response.getData();
                    tbOrderService.handlePartInfo(store);
                }
                else {
                    res = response;
                }
            }
            else {
                res.setResponseCode(ResponseCode.RESPONSE_ERROR);
                res.setDescription("save file " + filename + " failed!");
            }
        } catch (IOException e) {
            logger.error("file " + filename + " gets bytes failed: " + e.getMessage());
            res.setResponseCode(ResponseCode.RESPONSE_ERROR);
            res.setDescription("file gets bytes failed!");
            return res;
        }
        return res;
    }
}
