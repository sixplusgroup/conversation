package finley.gmair.service.impl;

import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.OrderItemDao;
import finley.gmair.model.order.OrderChannel;
import finley.gmair.model.order.OrderItem;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.dao.ChannelDao;
import finley.gmair.dao.OrderDao;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderService;
import finley.gmair.util.OrderExtension;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private LocationService locationService;

    @Override
    public ResultData fetchPlatformOrder(Map<String, Object> condition) {
        ResultData result = new ResultData();
        ResultData response = orderDao.queryOrder(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_NULL) {
            result.setResponseCode(ResponseCode.RESPONSE_NULL);
            result.setDescription("No platform order found");
        }
        if (response.getResponseCode() == ResponseCode.RESPONSE_ERROR) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to fetch platform order");
        }
        return result;
    }

    /**
     * This method will save the order record to database
     * step 1: save the order information
     * step 2: save the order item list
     *
     * @param order
     * @return
     */
    @Override
    public ResultData createPlatformOrder(PlatformOrder order) {
        ResultData result = new ResultData();
        //first store order and then store order item
        ResultData response = orderDao.insertOrder(order);
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Fail to store platform order with: " + order.toString());
        }
        String orderId = ((PlatformOrder) response.getData()).getOrderId();
        order.setOrderId(orderId);
        //insert order item
        List<OrderItem> list = order.getList();
        list.forEach(item -> orderItemDao.insert(item, orderId));
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        result.setData(order);
        return result;
    }

    @Override
    public ResultData updatePlatformOrder(PlatformOrder order) {
        ResultData result = new ResultData();
        ResultData response = orderDao.updateOrder(order);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            result.setResponseCode(ResponseCode.RESPONSE_OK);
            result.setData(response.getData());
        } else {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(new StringBuffer("Fail to update order with id: ").append(order.getOrderId()).append(" to ").append(order.toString()).toString());
        }
        return result;
    }

    @Override
    public ResultData process(MultipartFile file) {
        ResultData result = new ResultData();
        try {
            String filename = file.getOriginalFilename();
            String extension = filename.substring(filename.lastIndexOf(".") + 1);
            if (!StringUtils.isEmpty(extension) && !(OrderExtension.XLSX.equals(extension) || OrderExtension.XLS.equals(extension))) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("The file extension is not as expected");
                return result;
            }
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            List<PlatformOrder> list = process(workbook);
            result.setData(list);
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    private List<PlatformOrder> process(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet == null) {
            return null;
        }
        Row header = sheet.getRow(0);
        int[] index = index(header);
        if (index == null) {
            return null;
        }
        List<PlatformOrder> list = new ArrayList<>();
        int row = 1;
        Row current = sheet.getRow(row);
        while (current != null) {
            String channel = stringValue(0, index, current);
            //check whether the channel exist, if not, log it as a new channel
            new Thread(() -> {
                Map<String, Object> condition = new HashMap<>();
                condition.put("channelName", channel);
                condition.put("blockFlag", false);
                ResultData response = channelDao.queryChannel(condition);
                if (response.getResponseCode() == ResponseCode.RESPONSE_NULL)
                    channelDao.insertChannel(new OrderChannel(channel));
            });
            String number = stringValue(1, index, current);
            String model = stringValue(2, index, current);
            double quantity = doubleValue(3, index, current);
            Date date = dateValue(4, index, current);
            String username = stringValue(5, index, current);
            String phone;
            try {
                phone = stringValue(6, index, current);
            } catch (Exception e) {
                phone = String.valueOf(doubleValue(6, index, current));
            }
            String address = stringValue(7, index, current);
            String description = stringValue(8, index, current);
            OrderItem item = new OrderItem(model, quantity, 0);
            List<OrderItem> items = new ArrayList<>(Arrays.asList(item));
            ResultData response = locationService.geocoder(address);
            PlatformOrder order = new PlatformOrder(items, number, username, phone, address, channel, description);
            order.setCreateAt(new Timestamp(date.getTime()));
            if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                JSONObject location = ((JSONObject) response.getData()).getJSONObject("address_components");
                String provicne = location.getString("province");
                String city = location.getString("city");
                String district = location.getString("district");
                order.setLocation(provicne, city, district);
            }
            list.add(order);
        }
        return list;
    }

    private int[] index(Row row) {
        int[] index = new int[OrderExtension.HEADER.length];
        List<String> list = new ArrayList<>();
        int i = 0;
        Cell cell = row.getCell(i);
        do {
            list.add(cell.getStringCellValue().trim());
            i++;
            cell = row.getCell(i);
        } while (cell != null && !StringUtils.isEmpty(cell.getStringCellValue()));
        for (i = 0; i < index.length; i++) {
            index[i] = list.indexOf(OrderExtension.HEADER[i]);
            if (index[i] < 0) {
                return null;
            }
        }
        return index;
    }

    private String stringValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? "" : row.getCell(index[i]).getStringCellValue();
    }

    private double doubleValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? 0 : row.getCell(index[i]).getNumericCellValue();
    }

    private Date dateValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? Calendar.getInstance().getTime() : row.getCell(index[i]).getDateCellValue();
    }
}
