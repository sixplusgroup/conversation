package finley.gmair.controller;

import com.alibaba.excel.EasyExcel;
import finley.gmair.model.dto.TbOrderExcel;
import finley.gmair.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author ：tsl
 * @date ：Created in 2020/11/2 13:57
 * @description ：
 */

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    TradeService tradeServiceImpl;

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
}
