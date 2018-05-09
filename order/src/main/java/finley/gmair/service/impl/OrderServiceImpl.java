package finley.gmair.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.dao.ChannelDao;
import finley.gmair.dao.OrderDao;
import finley.gmair.dao.OrderItemDao;
import finley.gmair.dao.OrderLocationRetryCountDao;
import finley.gmair.form.express.ExpressOrderForm;
import finley.gmair.model.location.OrderLocationRetryCount;
import finley.gmair.model.order.OrderChannel;
import finley.gmair.model.order.OrderItem;
import finley.gmair.model.order.PlatformOrder;
import finley.gmair.service.ExpressService;
import finley.gmair.service.LocationService;
import finley.gmair.service.OrderService;
import finley.gmair.service.ReconnaissanceService;
import finley.gmair.util.IDGenerator;
import finley.gmair.util.OrderExtension;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.DecimalFormat;
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

    @Autowired
    private OrderLocationRetryCountDao orderLocationRetryCountDao;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private ReconnaissanceService reconnaissanceService;

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
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Please make sure the data is in the #1 sheet");
                return result;
            }
            Row header = sheet.getRow(0);
            int[] index = index(OrderExtension.HEADER, header);
            if (index == null) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Please make sure that you use the expected template");
                return result;
            }
            int[] expressIndex = index(OrderExtension.EXPRESS_HEADER, header);
            int[] machineIndex = index(OrderExtension.MACHINE_HEADER, header);
            if (machineIndex == null) {
                result.setResponseCode(ResponseCode.RESPONSE_ERROR);
                result.setDescription("Please make sure that you use the expected template");
                return result;
            }
            new Thread(() -> {
                List<PlatformOrder> list = process(workbook, index, expressIndex, machineIndex);
                new Thread(() -> list.forEach(item -> createPlatformOrder(item))).start();
            }).start();
        } catch (Exception e) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription(e.getMessage());
        }
        return result;
    }

    private List<PlatformOrder> process(Workbook workbook, int[] index, int[] expressIndex, int[] machineIndex) {
        Sheet sheet = workbook.getSheetAt(0);
        List<PlatformOrder> list = new ArrayList<>();
        int row = 1;
        Row current = sheet.getRow(row);
        while (current != null) {
            String channel = stringValue(0, index, current);
            //check whether the channel exist, if not, log it as a new channel
            if (!StringUtils.isEmpty(channel))
                new Thread(() -> {
                    Map<String, Object> condition = new HashMap<>();
                    condition.put("channelName", channel);
                    condition.put("blockFlag", false);
                    ResultData response = channelDao.queryChannel(condition);
                    if (response.getResponseCode() == ResponseCode.RESPONSE_NULL)
                        channelDao.insertChannel(new OrderChannel(channel));
                }).start();
            else break;
            String number = stringValue(1, index, current);
            String model = stringValue(2, index, current);
            double quantity = doubleValue(3, index, current);
            Date date = dateValue(4, index, current);
            String username = stringValue(5, index, current);
            String phone;
            try {
                phone = stringValue(6, index, current);
            } catch (Exception e) {
                phone = phoneValue(6, index, current);
            }
            String address = stringValue(7, index, current).replaceAll(" ", "");
            String description = stringValue(8, index, current);
            OrderItem item = new OrderItem(model, quantity, 0);
            List<OrderItem> items = new ArrayList<>(Arrays.asList(item));
            PlatformOrder order = new PlatformOrder(items, number, username, phone, address, channel, description);
            order.setCreateAt(new Timestamp(date.getTime()));
            order.setOrderId(IDGenerator.generate("ODR"));
            // use location service geocode address
            try {
                ResultData response = locationService.geocoder(address);
                if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
                    JSONObject location = (JSON.parseObject(JSON.toJSONString(response.getData()))).getJSONObject("address_components");
                    String province = location.getString("province");
                    String city = location.getString("city");
                    String district = location.getString("district");
                    order.setLocation(province, city, district);
                } else {
                    insertOrderLocationRetryCount(order.getOrderId(), 1);
                }
                //trigger the method no more than twice per second
                Thread.sleep(500);
            } catch (Exception e) {
                //leave the province, city, district empty
                insertOrderLocationRetryCount(order.getOrderId(), 1);
            }
            // use express service
            if (expressIndex != null) {
                String expressCompany = stringValue(0, expressIndex, current);
                String expressNo = "";
                try {
                    expressNo = stringValue(1, expressIndex, current);
                } catch (Exception e) {
                    expressNo = phoneValue(1, expressIndex, current);
                }
                if (!StringUtils.isEmpty(expressCompany) && !StringUtils.isEmpty(expressNo)) {
                    try {
                        ResultData expressResult = expressService.addOrder(order.getOrderId(), expressCompany, expressNo);
                    } catch (Exception e) {

                    }
                }
            }
            // create reconnaissance
            try {
                //if the order does not contains machine
                if (stringValue(0, machineIndex, current).equals("否")) {

                }
                //if the order contains machine, create install and reconnaissance form
                else {

                    ResultData reconnaissanceResult = reconnaissanceService.createReconnaissance(order.getOrderId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(order);
            current = sheet.getRow(++row);
        }
        return list;
    }

    private ResultData insertOrderLocationRetryCount(String orderId, int retryCount) {
        OrderLocationRetryCount orderLocationRetryCount = new OrderLocationRetryCount();
        orderLocationRetryCount.setOrderId(orderId);
        orderLocationRetryCount.setRetryCount(retryCount);
        orderLocationRetryCount.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return orderLocationRetryCountDao.insert(orderLocationRetryCount);
    }

    private int[] index(final String[] header, Row row) {
        int[] index = new int[header.length];
        List<String> list = new ArrayList<>();
        int i = 0;
        Cell cell = row.getCell(i);
        do {
            list.add(cell.getStringCellValue().trim());
            i++;
            cell = row.getCell(i);
        } while (cell != null && !StringUtils.isEmpty(cell.getStringCellValue()));
        for (i = 0; i < index.length; i++) {
            index[i] = list.indexOf(header[i]);
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

    private String phoneValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? "" : new DecimalFormat("#").format(row.getCell(index[i]).getNumericCellValue());
    }

    private Date dateValue(int i, int[] index, Row row) {
        return StringUtils.isEmpty(row.getCell(index[i])) ? Calendar.getInstance().getTime() : row.getCell(index[i]).getDateCellValue();
    }
}
