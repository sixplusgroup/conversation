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

    public final static String[] HEADER = {"收货人", "联系方式", "收货地址", "数量", "发货内容"};

    public static JSONArray decode(Sheet sheet) {
        JSONArray result = new JSONArray();
        int num = sheet.getLastRowNum() + 1;
        Row header = sheet.getRow(0);
        int[] index = index(HEADER, header);
        if (index == null) return result;
        for (int i = 1; i < num; i++) {
            Row current = sheet.getRow(i);
            if (current == null) break;
            String name = getCellValue(current.getCell(index[0]), String.class);
            String phone = getCellValue(current.getCell(index[1]), Integer.class);
            String address = getCellValue(current.getCell(index[2]), String.class);
            String quantity = getCellValue(current.getCell(index[3]), Integer.class);
            String model = getCellValue(current.getCell(index[4]), String.class);
            for (int j = 0; j < Integer.parseInt(quantity); j++) {
                JSONObject item = new JSONObject();
                item.put("name", name);
                item.put("phone", phone);
                item.put("address", address);
                item.put("model", model);
                result.add(item);
            }
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
