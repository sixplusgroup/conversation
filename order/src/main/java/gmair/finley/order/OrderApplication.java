package gmair.finley.order;

import gmair.finley.dao.OrderDao;
import gmair.finley.model.GmairOrder;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;

@SpringBootApplication
@ComponentScan({"gmair.finley.dao", "gmair.finley.controller", "gmair.finley.service"})
@RestController
@RequestMapping("/order")
public class OrderApplication {
    @Autowired
    private OrderDao orderDao;

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @RequestMapping("/import")
    public void loadOrder() {
        String path = "/Users/fan/Desktop/shanghai2.xlsx";
        try {
            Workbook workbook = WorkbookFactory.create(new FileInputStream(path));
            Sheet list = workbook.getSheetAt(0);
            for (int i = 0; i < 27; i++) {
                Row current = list.getRow(i);
                String orderId = current.getCell(0).getStringCellValue();
                String name = current.getCell(1).getStringCellValue();
                String province = current.getCell(2).getStringCellValue();
                String city = current.getCell(3).getStringCellValue();
                String district = current.getCell(4).getStringCellValue();
                String address = current.getCell(5).getStringCellValue();
                String phone;
                try {
                    phone = current.getCell(6).getStringCellValue();
                }catch (Exception e) {
                    phone = String.valueOf(current.getCell(6).getNumericCellValue());
                }
                GmairOrder order = new GmairOrder(orderId, name, province, city, district, address, phone);
                orderDao.insert(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
