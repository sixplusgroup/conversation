package finley.gmair.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: ExcelUtil
 * @Description: TODO
 * @Author fan
 * @Date 2019/3/27 4:02 PM
 */
public class ExcelUtil {

    public final static String[] HEADER = {"收货人", "联系方式", "收货地址", "数量", "发货内容", "备注", "订单来源","工单类型"};

    public final static String[] ORDERHEADER = {
            "订单编号",
            "活动名称",
            "设备名称",
            "消费者编号",
            "姓名",
            "联系方式",
            "地址",
            "试纸数量",
            "试纸单价",
            "试纸总价",
            "试纸实际价格",
            "使用日期",
            "优惠码",
            "订单状态",
            "机器码",
            "寄出快递单号",
            "寄出快递公司",
            "寄回快递单号",
            "寄回快递公司",
            "原价",
            "实际价格",
            "使用时长",
            "备注",
            "创建时间"};

    public static JSONArray decode(Sheet sheet) {
        JSONArray result = new JSONArray();
        int num = sheet.getLastRowNum() + 1;
        Row header = sheet.getRow(0);
        int[] index = index(HEADER, header);
        if (index == null) return result;
        for (int i = 1; i < num; i++) {
            Row current = sheet.getRow(i);
            if (current == null) break;
            String name = getCellValue(current.getCell(index[0]));
            String phone = getCellValue(current.getCell(index[1]), Integer.class);
            String address = getCellValue(current.getCell(index[2]));
            String quantity = getCellValue(current.getCell(index[3]), Integer.class);
            String model = getCellValue(current.getCell(index[4]));
            String description = getCellValue(current.getCell(index[5]), Integer.class);
            String source = getCellValue(current.getCell(index[6]));
            String assignType = getCellValue(current.getCell(index[7]));
            for (int j = 0; j < Integer.parseInt(quantity); j++) {
                JSONObject item = new JSONObject();
                item.put("name", name);
                item.put("phone", phone);
                item.put("address", address);
                item.put("model", model);
                item.put("description", description);
                item.put("source", source);
                item.put("assignType",assignType);
                result.add(item);
            }
        }
        return result;
    }

    public static JSONArray decodeDriftOrder(Sheet sheet) {
        JSONArray result = new JSONArray();
        int num = sheet.getLastRowNum() + 1;
        Row header = sheet.getRow(0);
        int[] index = index(ORDERHEADER, header);
        if (index == null) return result;
        for (int i = 1; i < num; i++) {
            Row current = sheet.getRow(i);
            if (current == null) break;
            String order_id = getCellValue(current.getCell(index[0]));
            String activity_name = getCellValue(current.getCell(index[1]));
            String equip_name = getCellValue(current.getCell(index[2]));
            String consumer_id = getCellValue(current.getCell(index[3]));
            String consignee = getCellValue(current.getCell(index[4]));
            String phone = getCellValue(current.getCell(index[5]));
            String express_address = getCellValue(current.getCell(index[6]));
            String quantity = getCellValue(current.getCell(index[7]));
            String item_price = getCellValue(current.getCell(index[8]));
            String item_total_price = getCellValue(current.getCell(index[9]));
            String item_real_price = getCellValue(current.getCell(index[10]));
            String expected_date = getCellValue(current.getCell(index[11]));
            String excode = getCellValue(current.getCell(index[12]));
            String status = getCellValue(current.getCell(index[13]));
            String machine_orderNo = getCellValue(current.getCell(index[14]));//修改字段
            String express_out_num = getCellValue(current.getCell(index[15]));//修改字段
            String express_out_company = getCellValue(current.getCell(index[16]));//修改字段
            String express_back_num = getCellValue(current.getCell(index[17]));
            String express_back_company = getCellValue(current.getCell(index[18]));
            String total_price = getCellValue(current.getCell(index[19]));
            String real_pay = getCellValue(current.getCell(index[20]));
            String interval_date = getCellValue(current.getCell(index[21]));
            String description = getCellValue(current.getCell(index[22]));//修改字段
            String create_time = getCellValue(current.getCell(index[23]));

            JSONObject item = new JSONObject();
            item.put("orderId", order_id);
            item.put("activityName", activity_name);
            item.put("equipName", equip_name);
            item.put("consumerId", consumer_id);
            item.put("consignee", consignee);
            item.put("phone", phone);
            item.put("expressAddress", express_address);
            item.put("quantity", quantity);
            item.put("itemPrice", item_price);
            item.put("itemTotalPrice", item_total_price);
            item.put("itemRealPrice", item_real_price);
            item.put("expectedDate", expected_date);
            item.put("excode", excode);
            item.put("status", status);
            item.put("machineOrderNo", machine_orderNo);
            item.put("expressOutNum", express_out_num);
            item.put("expressOutCompany", express_out_company);
            item.put("expressBackNum", express_back_num);
            item.put("expressBackCompany", express_back_company);
            item.put("totalPrice", total_price);
            item.put("realPay", real_pay);
            item.put("intervalDate", interval_date);
            item.put("description", description);
            item.put("createTime", create_time);
            result.add(item);
        }
        return result;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue();
        }
        if (type == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }

    private static String getCellValue(Cell cell, Class clazz) {
        if (cell == null) {
            return "";
        }
        CellType type = cell.getCellType();
        if (type == CellType.STRING) {
            return cell.getStringCellValue();
        }
        if (type == CellType.NUMERIC) {
            if (clazz == Integer.class) {
                return NumberToTextConverter.toText(cell.getNumericCellValue());
            }
        }
        if (type == CellType.BLANK) {
            return "";
        }
        return "";
    }



    private static int[] index(final String[] header, Row row) {
        int[] index = new int[header.length];
        List<String> list = new ArrayList<>();
        int num = row.getLastCellNum() + 1;
        for (int i = 0; i < num; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !StringUtils.isEmpty(cell.getStringCellValue()))
                list.add(cell.getStringCellValue().trim());
        }
        for (int i = 0; i < index.length; i++) {
            index[i] = list.indexOf(header[i]);
            if (index[i] < 0) {
                return null;
            }
        }
        return index;
    }
}
